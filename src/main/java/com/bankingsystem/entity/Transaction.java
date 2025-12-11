//package com.bankingsystem.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "transactions")
//@Data
//public class Transaction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER, BILL_PAYMENT
//    private Double amount;
//    private LocalDateTime timestamp;
//
//    @ManyToOne
//    @JoinColumn(name = "account_id")
//    private Account account;
//}


package com.bankingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Amount for transaction
    @Column(nullable = false)
    private BigDecimal amount;

    // ✅ Status: PENDING, APPROVED, REJECTED
    @Column(nullable = false)
    private String status = "PENDING";

    // ✅ Timestamp for transaction creation
//    @Column(nullable = false)
//    private LocalDateTime timestamp = LocalDateTime.now();
//    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;


    // ✅ From Account (for transfer)
    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    // ✅ To Account (for transfer)
    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    // ✅ Optional: Transaction type for clarity
    @Column(nullable = false)
    private String type; // TRANSFER, BILL_PAYMENT, etc.
}

