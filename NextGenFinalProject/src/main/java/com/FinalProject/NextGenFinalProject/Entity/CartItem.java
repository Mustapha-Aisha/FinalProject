package com.FinalProject.NextGenFinalProject.Entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class CartItem {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "cart_id")
   private Cart cart;

   @ManyToOne
   @JoinColumn(name = "product_id")
   private Product product;

   private int quantity;

//   public CartItem(Cart cart, Product product, int quantity) {
//   }
}
