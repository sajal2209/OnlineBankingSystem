package com.bankingsystem.service;

import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.entity.User;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.BillPaymentRepository;
import com.bankingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class BillPaymentService {

    private final BillPaymentRepository billPaymentRepository;
    private final AccountRepository accountRepository;

    public BillPaymentService(BillPaymentRepository billPaymentRepository, AccountRepository accountRepository) {
        this.billPaymentRepository = billPaymentRepository;
        this.accountRepository = accountRepository;
    }

    public BillPayment scheduleBillPayment(Long accountId, BillPayment bill) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Check balance

        if (account.getBalance().compareTo(bill.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }


        // Deduct amount
        account.setBalance(account.getBalance().subtract(bill.getAmount()));
        accountRepository.save(account);

        // Link account and save bill payment
        bill.setAccount(account);
//        bill.setTimestamp(LocalDateTime.now());
        return billPaymentRepository.save(bill);
    }

    public List<BillPayment> getBillPaymentsByAccount(Long accountId) {
        return billPaymentRepository.findByAccountId(accountId);
    }

    public List<BillPayment> getBillPaymentsByUser(Long userId) {
        return billPaymentRepository.findByUserId(userId);
    }
}
