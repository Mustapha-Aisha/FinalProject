package com.FinalProject.NextGenFinalProject.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> cartItems;


    private double totalPrice;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

    // Constructor for initializing the customer field
    public Cart(User user) {
        this.user = user;
        this.cartItems = new ArrayList<>();  // Example initialization
        this.totalPrice = updateTotalPrice();               // Example initialization
        this.creationDate = LocalDateTime.now();  // Example initialization
        this.modificationDate = LocalDateTime.now();  // Example initialization
    }

    public double updateTotalPrice() {
        totalPrice = 0;
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (product != null) {
                totalPrice += item.getQuantity() * product.getPrice();
            }
        }
        return totalPrice;
    }

//    public void addCartItem(CartItem cartItem) {
//        cartItems.add(cartItem);
//        cartItem.setCart(this);
//    }

    // ... other methods and constructors
}
