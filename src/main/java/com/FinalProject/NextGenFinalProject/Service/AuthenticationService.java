package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.Cart;
import com.FinalProject.NextGenFinalProject.Entity.Role;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Entity.Wallet;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;


    private final PasswordEncoder passwordEncoder;

    private final MyUserDetailsService myUserDetailsService;

    private final JwtService jwtService;
    private final RoleRepository roleRepo;
    private final WalletRepository walletRepo;
    private final CartRepository cartRepository;



    public AppResponse<User> createAdmin(UserRequest userRequest) {
        boolean check = userRepo.existsByEmail(userRequest.getEmail());
        if (check) throw new ApiException("User already exists");
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(create_Role ());

        userRepo.save(user);
        return new AppResponse<>(200, "Admin created successfully", user);
    }

    public Role create_Role (){
        Role role = roleRepo.findByName("ADMIN");

        if (role == null) {
            Role role1  = new Role();
            role1.setName("ADMIN");
            return role1;
        }
        return role;
    }

    public AppResponse<User> createUser(UserRequest userRequest) {
        boolean check = userRepo.existsByEmail(userRequest.getEmail());
        if (check) throw new ApiException("User already exists");
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(create_User_Role());

        userRepo.save(user);

        Wallet wallet= new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUser(user);
        walletRepo.save(wallet);

        user.setWallet(wallet);





        userRepo.save(user);
        return new AppResponse<>(200, "User created successfully", user);
    }

    public Role create_User_Role(){
        Role role = roleRepo.findByName("USER");

        if (role == null) {
            Role role1  = new Role();
            role1.setName("USER");
            return role1;
        }
        return role;
    }

    public AppResponse<String> login( AuthenticationRequest request) {
        var user = myUserDetailsService.loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AppResponse<>(204, "Wrong email/password");
        }
        var jwtToken = jwtService.generateToken(user);
        return new AppResponse<>(0, "Successfully logged in", jwtToken);
    }

    public AppResponse<String> ResetPassword(AuthenticationRequest request){
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(()-> new ApiException("User doesn't exist"));
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepo.save(user);
           return new AppResponse<>("Password updated successfully");
        }
    }







//redo the login for everyone to get a token regardless of role