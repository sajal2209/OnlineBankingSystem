package com.bankingsystem.service;

import com.bankingsystem.dto.BillPaymentRequest;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.enums.PaymentStatus;
import com.bankingsystem.enums.PaymentType;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.BillPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BillPaymentService {
    private final BillPaymentRepository billPaymentRepository;
    private final AccountRepository accountRepository;

    public BillPaymentService(BillPaymentRepository billPaymentRepository, AccountRepository accountRepository) {
        this.billPaymentRepository = billPaymentRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public String payBill(BillPaymentRequest request, String username) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Optionally enforce ownership if Account has User and username is provided
        if (username != null && account.getUser() != null && !username.equals(account.getUser().getUsername())) {
            throw new RuntimeException("You can only pay bills from your own account.");
        }

        if (account.getUser() != null && !account.getUser().isActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        BillPayment billPayment = new BillPayment();
        billPayment.setBillerName(request.getBillerName());
        billPayment.setAmount(request.getAmount());
        billPayment.setAccount(account);
        // Set as immediate completed payment
        billPayment.setPaymentType(PaymentType.IMMEDIATE);
        billPayment.setStatus(PaymentStatus.COMPLETED);
        // createdAt is handled by @CreationTimestamp

        billPaymentRepository.save(billPayment);

        return "Bill payment successful";
    }

    public List<BillPayment> getBillPayments(Long accountId) {
        return billPaymentRepository.findByAccountId(accountId);
    }
}
