package com.FinalProject.NextGenFinalProject.Controller;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
   private final AuthenticationService authenticationService;

    @PostMapping("/admin-signup")
    public AppResponse<User> createAdmin(@Valid @RequestBody UserRequest request) {
        return authenticationService.createAdmin(request);
    }

    @PostMapping("/register")
    public AppResponse<User> createUser(@RequestBody @Valid UserRequest customerRequest) {
        return authenticationService.createUser(customerRequest);
    }



    public AppResponse<String> login(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return authenticationService.login(authenticationRequest);
    }


    @PatchMapping("/reset")
    public AppResponse<String> resetPass(@RequestBody @Valid AuthenticationRequest request){
        return authenticationService.ResetPassword(request);
    }



}
