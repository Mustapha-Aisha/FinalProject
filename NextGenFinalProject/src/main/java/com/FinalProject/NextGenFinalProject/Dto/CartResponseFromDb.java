package com.FinalProject.NextGenFinalProject.Dto;

import com.FinalProject.NextGenFinalProject.Entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseFromDb {
    private Long id;
    private List<CartItemFromDb> cartItems;
    private double totalPrice;

//    public CartResponseFromDb(Cart cart) {
//        id = cart.getId();
//        cartItems = cart.getCartItems().stream()
//                .map(CartItemFromDb::new)
//                .collect(Collectors.toList());
//        totalPrice = cart.getTotalPrice();
//    }
}
