package com.FinalProject.NextGenFinalProject.Entity;

import com.FinalProject.NextGenFinalProject.Dto.CartRequest;
import lombok.*;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private double orderAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//
//    public Order(String firstName, String lastName, String phoneNumber, String address, String postalCode, double orderAmount, String orderStatus, Product product, User user) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//        this.postalCode = postalCode;
//        this.orderAmount = orderAmount;
//        this.orderStatus = orderStatus;
//        this.product = product;
//        this.user = user;
//    }
}
