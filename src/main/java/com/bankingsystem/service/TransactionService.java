package com.bankingsystem.service;

import com.bankingsystem.entity.Transaction;
import com.bankingsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) { this.transactionRepository = transactionRepository; }

//    public List<Transaction> getTransactionsByAccount(Long accountId) {
//        return transactionRepository.findByAccountId(accountId);
//    }
}
