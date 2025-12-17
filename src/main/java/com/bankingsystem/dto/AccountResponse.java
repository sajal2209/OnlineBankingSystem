
package com.bankingsystem.dto;

import com.bankingsystem.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private AccountType type;
    private BigDecimal balance;
}

