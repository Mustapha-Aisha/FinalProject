package com.FinalProject.NextGenFinalProject.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User user;
    @OneToMany
    private List<Product> products;
    private double totalPrice;

    private String shippingAddress;

    private String orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String paymentInformation;
    private Transaction transaction;
}
