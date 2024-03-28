package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;


public interface PaystackService {
    CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception;

    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);
    PaymentVerificationResponse paymentVerification(String reference) throws Exception;

    boolean verifySignature(String secretKey, String rawJson, String xPaystackSignature);
}