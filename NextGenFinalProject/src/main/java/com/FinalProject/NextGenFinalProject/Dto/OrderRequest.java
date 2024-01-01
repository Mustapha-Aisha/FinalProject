package com.FinalProject.NextGenFinalProject.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private double orderAmount;
    private String orderStatus;
    private List<CartRequest.ItemRequest> itemRequests;

}
