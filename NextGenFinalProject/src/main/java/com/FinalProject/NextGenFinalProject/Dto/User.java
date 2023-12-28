package com.FinalProject.NextGenFinalProject.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String userName;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String dateOfBirth;

    public User(com.FinalProject.NextGenFinalProject.Entity.User user){
        id = user.getId();
        userName = user.getUserName();
        email = user.getEmail();
        password = user.getPassword();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        address = user.getAddress();
        phoneNumber = user.getPhoneNumber();
        dateOfBirth = user.getDateOfBirth();
    }
}