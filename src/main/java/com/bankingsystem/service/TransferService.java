package com.bankingsystem.service;

import com.bankingsystem.entity.Account;
import com.bankingsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) { this.accountRepository = accountRepository; }

    public void transferFunds(Long fromAccountId, Long toAccountId, double amount) {
        Account from = accountRepository.findById(fromAccountId).orElseThrow(() -> new RuntimeException("From account not found"));
        Account to = accountRepository.findById(toAccountId).orElseThrow(() -> new RuntimeException("To account not found"));
        if (from.getBalance().doubleValue() < amount) throw new RuntimeException("Insufficient balance");
        from.setBalance(from.getBalance().subtract(BigDecimal.valueOf(amount)));
        to.setBalance(to.getBalance().add(BigDecimal.valueOf(amount)));
        accountRepository.save(from);
        accountRepository.save(to);
    }
}
