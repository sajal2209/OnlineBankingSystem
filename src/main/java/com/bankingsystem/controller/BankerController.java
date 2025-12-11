
package com.bankingsystem.controller;

import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.Transaction;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banker")
public class BankerController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public BankerController(UserRepository userRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/customers")
    public List<User> getAllCustomers() {
        return userRepository.findByRole("CUSTOMER");
    }

    @PostMapping("/customers/{id}/deactivate")
    public String deactivateCustomer(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return "Customer deactivated successfully!";
    }

    @PostMapping("/customers/{id}/activate")
    public String activateCustomer(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
        return "Customer activated successfully!";
    }

    @GetMapping("/transactions/pending")
    public List<Transaction> getPendingTransactions() {
        return transactionRepository.findByStatus("PENDING");
    }

    @PostMapping("/transactions/{id}/approve")
    public String approveTransaction(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Account fromAccount = transaction.getFromAccount();
        Account toAccount = transaction.getToAccount();

        if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            return "Insufficient funds";
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transaction.setStatus("COMPLETED");
        transactionRepository.save(transaction);

        return "Transaction approved successfully!";
    }

    @PostMapping("/transactions/{id}/reject")
    public String rejectTransaction(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus("REJECTED");
        transactionRepository.save(transaction);
        return "Transaction rejected successfully!";
    }
}
