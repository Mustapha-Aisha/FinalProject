package com.FinalProject.NextGenFinalProject.Dto;
import com.FinalProject.NextGenFinalProject.Entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItemFromDb {

        private ProductResponseFromDb product;
        private int quantity;



}
