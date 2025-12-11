package com.bankingsystem.entity;

import com.bankingsystem.enums.Frequency;
import com.bankingsystem.enums.PaymentStatus;
import com.bankingsystem.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "bill_payments")
//@Data
//public class BillPayment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String billerName;
//    private BigDecimal amount;
//    private LocalDateTime timestamp;
//
//    @ManyToOne
//    @JoinColumn(name = "account_id")
//    private Account account;
//}




@Entity
@Table(name = "bill_payments")
@Data
public class BillPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String billerName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType; // IMMEDIATE or RECURRING

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED, FAILED, CANCELLED

    @Enumerated(EnumType.STRING)
    private Frequency frequency; // DAILY, WEEKLY, MONTHLY (for recurring)

    private LocalDateTime nextPaymentDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}





