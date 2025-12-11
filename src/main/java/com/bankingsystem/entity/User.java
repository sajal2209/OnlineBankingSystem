package com.bankingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;

    private boolean active = true; // ✅ Default active


    @OneToMany(mappedBy = "user")
    private Set<Account> accounts;
}
