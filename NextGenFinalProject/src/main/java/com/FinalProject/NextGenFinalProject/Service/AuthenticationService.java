package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.Cart;
import com.FinalProject.NextGenFinalProject.Entity.Role;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.CartRepository;
import com.FinalProject.NextGenFinalProject.Repository.ProductRepository;
import com.FinalProject.NextGenFinalProject.Repository.RoleRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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
        Cart cart = new Cart();
        // Set any necessary properties for the cart
        cartRepository.save(cart);

        user.setCart(cart);

        userRepo.save(user);


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
    }






//redo the login for everyone to get a token regardless of role