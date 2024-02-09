package com.FinalProject.NextGenFinalProject.Entity;

;
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
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int rating;
    private String comment;
    private LocalDate dateSubmitted;



}
