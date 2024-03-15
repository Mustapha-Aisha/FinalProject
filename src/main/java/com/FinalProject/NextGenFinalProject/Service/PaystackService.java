package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;

import javax.transaction.Transactional;

public interface PaystackService {
    CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception;
    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);
//    PaymentVerificationResponse paymentVerification(rificationDto) throws Exception;

    @Transactional
    PaymentVerificationResponse paymentVerification(String reference) throws Exception;
}