package com.FinalProject.NextGenFinalProject.Entity;

import com.FinalProject.NextGenFinalProject.Dto.ProductResponseFromDb;
import com.FinalProject.NextGenFinalProject.Dto.UserResponseFromDb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private UserResponseFromDb user;

    @ManyToOne
    private ProductResponseFromDb product;
    private int rating;
    private String comment;
    private LocalDate dateSubmitted;



}
