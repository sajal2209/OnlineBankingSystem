package com.bankingsystem.controller;

import com.bankingsystem.entity.Transaction;
import com.bankingsystem.entity.User;
import com.bankingsystem.service.BankerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banker")
public class BankerController {

    private final BankerService bankerService;

    public BankerController(BankerService bankerService) {
        this.bankerService = bankerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(bankerService.getAllCustomers());
    }

    @PostMapping("/customers/{id}/deactivate")
    public ResponseEntity<String> deactivateCustomer(@PathVariable Long id) {
        try {
            bankerService.deactivateCustomer(id);
            return ResponseEntity.ok("Customer deactivated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/customers/{id}/activate")
    public ResponseEntity<String> activateCustomer(@PathVariable Long id) {
        try {
            bankerService.activateCustomer(id);
            return ResponseEntity.ok("Customer activated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/transactions/pending")
    public ResponseEntity<List<Transaction>> getPendingTransactions() {
        return ResponseEntity.ok(bankerService.getPendingTransactions());
    }

    @PostMapping("/transactions/{id}/approve")
    public ResponseEntity<String> approveTransaction(@PathVariable Long id) {
        try {
            String res = bankerService.approveTransaction(id);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transactions/{id}/reject")
    public ResponseEntity<String> rejectTransaction(@PathVariable Long id) {
        try {
            String res = bankerService.rejectTransaction(id);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
