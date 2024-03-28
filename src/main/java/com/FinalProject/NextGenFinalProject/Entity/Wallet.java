package com.FinalProject.NextGenFinalProject.Entity;

import com.FinalProject.NextGenFinalProject.Dto.UserResponseFromDb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "wallet")

public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToOne
    private User user;
}
