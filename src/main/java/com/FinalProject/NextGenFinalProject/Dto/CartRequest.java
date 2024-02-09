package com.FinalProject.NextGenFinalProject.Dto;

import lombok.Data;

import java.util.List;

@Data
public class CartRequest {
    private List<ItemRequest> itemRequest;



    @Data
    public static class ItemRequest{
        private Long productId;
        private int quantity;
    }
}
