package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Config.APIConstants;
import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.PaymentPaystack;
import com.FinalProject.NextGenFinalProject.Repository.PaystackPaymentRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
@AllArgsConstructor
public class PaystackServiceImpl implements PaystackService {

    private final PaystackPaymentRepository paystackPaymentRepository;
    private final UserRepo appUserRepository;

    //    @Value("${secret.key}")
    private static final String secretKey = "sk_live_adb0217c8d619d8169757bd5e546e5eee2075601";

    @Override
    public CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception {
        CreatePlanResponse createPlanResponse = null;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            Gson gson = new Gson();
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
            StringEntity postingString = new StringEntity(gson.toJson(initializePaymentDto));
            HttpPost post = new HttpPost(APIConstants.PAYSTACK_INIT);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + secretKey);
            post.addHeader("User-Agent", "My own REST client");  // Add this line

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
    @Transactional
    public PaymentVerificationResponse paymentVerification(String reference) throws Exception {
        PaymentVerificationResponse paymentVerificationResponse = null;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(APIConstants.PAYSTACK_VERIFY + "/" + reference);
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

                if (paymentVerificationResponse == null || !"false".equals(paymentVerificationResponse.getStatus())) {
                    String r = EntityUtils.toString(response.getEntity()); //
                    throw new Exception("An error occurred during Paystack payment verification" + r);
                } else if ("success".equals(paymentVerificationResponse.getData().getStatus())) {
                    // Payment verification success
                    System.out.println("Payment verification success");
                }
            } else {
                throw new IOException("Paystack is unable to verify payment at the moment. HTTP Status Code: " +
                        response.getStatusLine().getStatusCode());
            }
        } catch (IOException ex) {
            // Log the exception using a logging framework
            ex.printStackTrace();
            throw new IOException("Error during Paystack payment verification: " + ex.getMessage());
        }

        // Perform any additional processing or save to the database as needed
        // ...

        return paymentVerificationResponse;
    }
}
