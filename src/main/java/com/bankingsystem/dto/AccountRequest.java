
//package com.bankingsystem.dto;
//
//import lombok.Data;
//
//import java.math.BigDecimal;
//@Data
//public class AccountRequest {
//    private String accountNumber;
//    private BigDecimal balance;
//    private Long userId;
//
//    // Getters and Setters
//}

//
//package com.bankingsystem.dto;
//
//import lombok.Data;
//
//import java.math.BigDecimal;
//@Data
//public class AccountRequest {
//    private String accountNumber;
//    private BigDecimal balance;
//    private Long userId;
//
//    // Getters and Setters
//}

package com.bankingsystem.dto;

import com.bankingsystem.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank
    @Size(min = 6, max = 20)
    private String accountNumber;

    @NotNull
    private AccountType type; // SAVINGS or CURRENT

    @NotNull
    private Long userId;

    // Optional: initial deposit on account opening
    @PositiveOrZero
    private BigDecimal balance = BigDecimal.ZERO;
}
