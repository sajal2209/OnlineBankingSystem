package com.bankingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
