
package com.bankingsystem.dto;

import lombok.Data;

import java.math.BigDecimal;
//@Data
//public class BillPaymentRequest {
//    private String accountNumber;
//    private String billerName;
//    private BigDecimal amount;
//
//    // Getters and Setters
//}



public class BillPaymentRequest {
    private String accountNumber;
    private String billerName;
    private BigDecimal amount;

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

