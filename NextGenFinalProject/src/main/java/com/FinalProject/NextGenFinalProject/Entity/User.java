package com.FinalProject.NextGenFinalProject.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "dateOfBirth", nullable = false)
    private String dateOfBirth;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Role role;

    @OneToOne(cascade = CascadeType.PERSIST) // One Customer can have one Cart
    @JoinColumn(name = "cart_id", referencedColumnName = "id") // Foreign key column in Cart
    private Cart cart;



}
