package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Config.APIConstants;
import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.PaymentPaystack;
import com.FinalProject.NextGenFinalProject.Repository.PaystackPaymentRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.jandex.Main;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class PaystackServiceImpl implements PaystackService {

    private final PaystackPaymentRepository paystackPaymentRepository;
    private final UserRepo appUserRepository;
    public static String getSecretKey() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
            return properties.getProperty("secretKey");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception {
        CreatePlanResponse createPlanResponse = null;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            Gson gson = new Gson();
            String secretKey = getSecretKey();
            if (secretKey == null || secretKey.isEmpty()) {

                throw new IllegalArgumentException("Secret key is null or empty.");
            } System.out.println(secretKey);
            StringEntity postingString = new StringEntity(gson.toJson(createPlanDto));
            HttpPost post = new HttpPost(APIConstants.PAYSTACK_INIT);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == APIConstants.STATUS_CODE_CREATED) {
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                }
            } else {
                System.out.println("Error response body: " + EntityUtils.toString(response.getEntity()));

                throw new Exception("Paystack is unable to process payment at the moment " +
                        "or something wrong with the request");
            }

            ObjectMapper mapper = new ObjectMapper();
            createPlanResponse = mapper.readValue(result.toString(), CreatePlanResponse.class);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return createPlanResponse;
    }

    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto) {
        InitializePaymentResponse initializePaymentResponse = null;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            Gson gson = new Gson();
            String secretKey = getSecretKey();
            StringEntity postingString = new StringEntity(gson.toJson(initializePaymentDto));
            HttpPost post = new HttpPost(APIConstants.PAYSTACK_INIT);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + secretKey);
//            post.addHeader("User-Agent", "My own REST client");  // Add this line

            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == APIConstants.STATUS_CODE_OK) {
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                }
            } else {
                System.out.println("Error status code: " + response.getStatusLine().getStatusCode());
                System.out.println("Error response body: " + EntityUtils.toString(response.getEntity()));
                throw new Exception("Paystack is unable to initialize payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            initializePaymentResponse = mapper.readValue(result.toString(), InitializePaymentResponse.class);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return initializePaymentResponse;
    }




    @Override
    public PaymentVerificationResponse paymentVerification(String reference) throws Exception {
        PaymentVerificationResponse paymentVerificationResponse = null;
        String secretKey = getSecretKey();

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(APIConstants.PAYSTACK_VERIFY + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + secretKey);

            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == APIConstants.STATUS_CODE_OK) {
                // Process the response
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Paystack Verification Response: " + responseBody);

                // Parse the response using ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                paymentVerificationResponse = mapper.readValue(responseBody, PaymentVerificationResponse.class);
                if ("success".equals(paymentVerificationResponse.getData().getStatus())) {
                    System.out.println("Payment verification success");
                }else {
                    throw new Exception("An error occurred during Paystack payment verification: " + responseBody);
                }
            } else {
                String errorMessage = EntityUtils.toString(response.getEntity());
                throw new IOException("Paystack is unable to verify payment at the moment. HTTP Status Code: " +
                        response.getStatusLine().getStatusCode() + ". Error Message: " + errorMessage);
            }
        } catch (IOException ex) {
            // Log the exception using a logging framework
            ex.printStackTrace();
            throw new IOException("Error during Paystack payment verification: " + ex.getMessage());
        }

        return paymentVerificationResponse;
    }


    @Override
    public boolean verifySignature(String secretKey, String rawJson, String xPaystackSignature) {
        try {
            String HMAC_SHA512 = "HmacSHA512";
            secretKey = getSecretKey();

            byte[] byteKey = secretKey.getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte[] mac_data = sha512_HMAC.doFinal(rawJson.getBytes("UTF-8"));
            String calculatedSignature = DatatypeConverter.printHexBinary(mac_data).toLowerCase();
            return calculatedSignature.equals(xPaystackSignature.toLowerCase());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            // Handle exceptions
            e.printStackTrace();
            return false;
        }
    }
}
