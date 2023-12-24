package com.FinalProject.NextGenFinalProject.Dto;

import com.FinalProject.NextGenFinalProject.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseFromDb {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String brand;
    private String imageURL;
    private int quantity;
    private Boolean isAvailable;

    public ProductResponseFromDb(Product product){
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        brand = product.getBrand();
        category = String.valueOf(product.getCategory());
        quantity = product.getQuantity();
        isAvailable = product.getIsAvailable();
    }




}
