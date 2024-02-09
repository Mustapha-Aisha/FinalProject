package com.FinalProject.NextGenFinalProject.Dto;

import com.FinalProject.NextGenFinalProject.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotEmpty(message = "name cant be blank")
    private String name;

    @NotEmpty(message = "description cant be blank")
    private String description;

    @NotNull(message = "price cannot be null")
    private double price;

    @NotNull(message = "category cant be blank")
    private Category category;

    @NotEmpty(message = "brand cant be blank")
    private String brand;

    @NotEmpty(message = "imageurl cant be blank")
    private String imageURL;

    @NotNull(message = "price cannot be null")
    private int quantity;


//    private List<String> review:
}
