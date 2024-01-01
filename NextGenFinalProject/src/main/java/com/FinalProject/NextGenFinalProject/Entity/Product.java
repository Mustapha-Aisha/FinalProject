package com.FinalProject.NextGenFinalProject.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "imageUrl", nullable = false)
    private String imageURL;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "isAvailable", nullable = false)
    private Boolean isAvailable;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

//    @OneToOne(cascade = CascadeType.PERSIST)
//    private Cart cart;


}


