package com.bankingsystem.controller;

import com.bankingsystem.entity.Transaction;
import com.bankingsystem.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) { this.transactionService = transactionService; }

//    @GetMapping("/{accountId}")
//    public List<Transaction> getTransactions(@PathVariable Long accountId) {
//        return transactionService.getTransactionsByAccount(accountId);
//    }
}
