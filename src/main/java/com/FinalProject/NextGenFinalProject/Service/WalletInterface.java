package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.AppResponse;
import com.FinalProject.NextGenFinalProject.Dto.InitializePaymentDto;
import com.FinalProject.NextGenFinalProject.Dto.InitializePaymentResponse;

public interface WalletInterface {
    InitializePaymentResponse InitiatingWalletFunding (InitializePaymentDto dto) throws  Exception;
}
