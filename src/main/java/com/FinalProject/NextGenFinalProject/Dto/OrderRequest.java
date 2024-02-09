package com.FinalProject.NextGenFinalProject.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotEmpty(message = "firstName cant be blank")
    private String firstName;

    @NotEmpty(message = "lastName cant be blank")
    private String lastName;

    @NotEmpty(message = "phoneNumber cant be blank")
    private String phoneNumber;

    @NotEmpty(message = "address cant be blank")
    private String address;

    @NotEmpty(message = "postalCode cant be blank")
    private String postalCode;

    @NotEmpty(message = "orderAmount cant be blank")
    private double orderAmount;

    @NotEmpty(message = "orderStatus cant be blank")
    private String orderStatus;

    @NotEmpty(message = "itemRequests cant be blank")
    private List<CartRequest.ItemRequest> itemRequests;

}
