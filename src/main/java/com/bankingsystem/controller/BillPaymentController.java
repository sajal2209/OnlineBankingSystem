
package com.bankingsystem.controller;

import com.bankingsystem.dto.BillPaymentRequest;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.BillPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/billpay")
public class BillPaymentController {

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity<String> payBill(@RequestBody BillPaymentRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        BillPayment billPayment = new BillPayment();
        billPayment.setBillerName(request.getBillerName());
        billPayment.setAmount(request.getAmount());
        billPayment.setAccount(account);
//        billPayment.setTimestamp(LocalDateTime.now());

        billPaymentRepository.save(billPayment);

        return ResponseEntity.ok("Bill payment successful");
    }

    @GetMapping("/history/{accountId}")
    public List<BillPayment> getBillPayments(@PathVariable Long accountId) {
        return billPaymentRepository.findByAccountId(accountId);
    }
}
