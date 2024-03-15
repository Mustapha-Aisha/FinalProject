package com.FinalProject.NextGenFinalProject.Controller;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Service.PaystackService;
import com.FinalProject.NextGenFinalProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ExposedController {

    private final UserService userService;
    private final PaystackService paystackService;

    @GetMapping("/getProducts")
    public AppResponse<Map<String, Object>> getAllProd(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") int size
    ){
        return userService.getAllProducts(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public AppResponse<Product> getProdById(@PathVariable Long id){
        return userService.getProductById(id);
    }

    @PostMapping("/createplan")
    public CreatePlanResponse createPlan (@Validated @RequestBody CreatePlanDto createPlanDto) throws Exception {
        return paystackService.createPlan(createPlanDto);
    }

    @PostMapping("/initializepayment")
    public InitializePaymentResponse initializePayment(@Validated @RequestBody InitializePaymentDto initializePaymentDto) throws Throwable {
        return paystackService.initializePayment(initializePaymentDto);
    }

    @GetMapping("/verify/{reference}") //when testing with postman remove the braces around reference
    public PaymentVerificationResponse paymentVerification(@PathVariable String reference  ) throws Exception {


        return paystackService.paymentVerification(reference);
    }

}
