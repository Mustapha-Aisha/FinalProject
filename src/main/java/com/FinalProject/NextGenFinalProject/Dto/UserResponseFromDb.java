package com.FinalProject.NextGenFinalProject.Dto;

import com.FinalProject.NextGenFinalProject.Entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseFromDb {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date createdOn;
    private BigDecimal walletBalance;

    public UserResponseFromDb(User user){
       id= user.getId();
       email = user.getEmail();
       firstName = user.getFirstName();
       lastName = user.getLastName();
       phoneNumber = user.getPhoneNumber();
       createdOn = user.getCreatedOn();
       walletBalance = user.getWalletBalance();
    }


}
