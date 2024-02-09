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

   @ManyToOne(cascade = CascadeType.PERSIST)
   @JoinColumn(name = "cart_id", referencedColumnName = "id")
   private Cart cart;

   @ManyToOne(cascade = CascadeType.PERSIST)
//   @JoinColumn(name = "product_id")
   private Product product;

   private int quantity;




}
