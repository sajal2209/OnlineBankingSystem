package com.bankingsystem.service;

import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.Transaction;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankerService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public BankerService(UserRepository userRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<User> getAllCustomers() {
        return userRepository.findByRole("CUSTOMER");
    }

    @Transactional
    public User deactivateCustomer(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        return userRepository.save(user);
    }

    @Transactional
    public User activateCustomer(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        return userRepository.save(user);
    }

    public List<Transaction> getPendingTransactions() {
        return transactionRepository.findByStatus("PENDING");
    }

    @Transactional
    public String approveTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Account fromAccount = transaction.getFromAccount();
        Account toAccount = transaction.getToAccount();

        if (fromAccount == null || toAccount == null) {
            throw new RuntimeException("Invalid transaction accounts");
        }

        if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transaction.setStatus("COMPLETED");
        transactionRepository.save(transaction);

        return "Transaction approved successfully!";
    }

    @Transactional
    public String rejectTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus("REJECTED");
        transactionRepository.save(transaction);
        return "Transaction rejected successfully!";
    }
}

