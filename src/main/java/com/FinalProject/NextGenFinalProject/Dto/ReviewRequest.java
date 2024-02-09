package com.FinalProject.NextGenFinalProject.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

        private Long productId;
        private int rating;
        private String comment;




}
