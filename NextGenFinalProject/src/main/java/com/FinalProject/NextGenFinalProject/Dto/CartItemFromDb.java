package com.FinalProject.NextGenFinalProject.Dto;
import com.FinalProject.NextGenFinalProject.Entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItemFromDb {

        private Long id;
        private ProductResponseFromDb product;
        private int quantity;

        public CartItemFromDb(CartItem cartItem) {
            id = cartItem.getId();
            product = new ProductResponseFromDb(cartItem.getProduct());
            quantity = cartItem.getQuantity();
        }


}
