package com.bankingsystem.controller;

import com.bankingsystem.dto.AccountRequest;
import com.bankingsystem.dto.BillPaymentRequest;
import com.bankingsystem.dto.RecurringPaymentRequest;
import com.bankingsystem.dto.TransferRequest;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.entity.Transaction;
import com.bankingsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransferRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return accountService.transferFunds(request, username);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAccounts(@PathVariable Long userId) {
        try {
            List<Account> accounts = accountService.getAccountsByUser(userId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserTransactions(@PathVariable Long userId) {
        List<Map<String, Object>> response = accountService.getUserTransactions(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<Transaction> transactions = accountService.getTransactions(userId, type, status, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/billpay")
    public ResponseEntity<String> payBill(@RequestBody BillPaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return accountService.payBill(request, username);
    }

    @PostMapping("/billpay/schedule")
    public ResponseEntity<String> scheduleRecurringPayment(@RequestBody RecurringPaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return accountService.scheduleRecurringPayment(request, username);
    }

    @GetMapping("/billpay/scheduled/{userId}")
    public ResponseEntity<List<BillPayment>> getScheduledPayments(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getScheduledPayments(userId));
    }

    @DeleteMapping("/billpay/{id}")
    public ResponseEntity<String> cancelScheduledPayment(@PathVariable Long id) {
        return accountService.cancelScheduledPayment(id);
    }

}
 