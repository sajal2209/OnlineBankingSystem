
package com.bankingsystem.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountRequest {
    private String accountNumber;
    private BigDecimal balance;
    private Long userId;

    // Getters and Setters
}
