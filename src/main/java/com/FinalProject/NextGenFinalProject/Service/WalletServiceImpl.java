package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Entity.Wallet;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import com.FinalProject.NextGenFinalProject.Repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletInterface  {

    private final PaystackServiceImpl paystackService;
    private final WalletRepository walletRepository;
    private final UserRepo userRepo;
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
    public InitializePaymentResponse InitiatingWalletFunding(InitializePaymentDto paymentDto) {
        try {
            String callback_url = " https://ff72-41-86-152-178.ngrok-free.app/user/callback";

            paymentDto.setCallback_url(callback_url);

            // Initialize payment request using Paystack API
            InitializePaymentResponse response = paystackService.initializePayment(paymentDto);

            return  response;
        } catch (Exception e) {
            throw new ApiException("Error initiating payment: " + e.getMessage());
        }
    }
    public AppResponse<String> handleCallback(String payload , String xPaystackSignature) throws JsonProcessingException {
        String secretKey = getSecretKey();
        if (paystackService.verifySignature(secretKey, payload, xPaystackSignature)) {
            ObjectMapper mapper = new ObjectMapper();
            PaymentVerificationResponse response = mapper.readValue(payload, PaymentVerificationResponse.class);
            String email = response.getData().getCustomer().getEmail();
            BigDecimal amountPaid = response.getData().getAmount();

            User u = userRepo.findByEmail(email).orElse(null);
            UserResponseFromDb us = new UserResponseFromDb(u);
            // Find the user and update the wallet
            Wallet user = walletRepository.findByUser(u);
            if (user != null) {
                Wallet wallet = u.getWallet();
                if (wallet == null) {
                    wallet = new Wallet();
                    wallet.setUser(u);
                }
                BigDecimal currentBalance = wallet.getBalance();
                BigDecimal newBalance = currentBalance.add(amountPaid);
                wallet.setBalance(newBalance);
                walletRepository.save(wallet);
            } else {
                // User not found, handle this case accordingly
                throw new ApiException("User with email " + email + " not found.");
            }
        } else {
            throw new ApiException("Signature verification failed.");
        }

        return new AppResponse<>(200, "Your wallet has been updated successfully");
    }



}
