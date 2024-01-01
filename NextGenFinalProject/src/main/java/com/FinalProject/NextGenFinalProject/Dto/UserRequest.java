package com.FinalProject.NextGenFinalProject.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
//    @NotEmpty(message = "userName cant be blank")
//    private String userName;

    @NotEmpty(message = "email cant be blank")
    private String email;

    @NotEmpty(message = "password cant be blank")
    private String password;

    @NotEmpty(message = "firstName cant be blank")
    private String firstName;
    @NotEmpty(message = "lastName cant be blank")
    private String lastName;
//    @NotEmpty(message = "address cant be blank")
//    private String address;
    @NotEmpty(message = "phoneNumber cant be blank")
    private String PhoneNumber;
//    @NotEmpty(message = "dateOfBirth cant be blank")
//    private String dateOfBirth;

}
