package com.bankingsystem.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // CUSTOMER, BANKER, ADMIN

    // Getters and Setters
}
