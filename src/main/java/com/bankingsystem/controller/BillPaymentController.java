package com.bankingsystem.controller;

import com.bankingsystem.dto.BillPaymentRequest;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.service.BillPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billpay")
public class BillPaymentController {

    @Autowired
    private BillPaymentService billPaymentService;

    @PostMapping
    public ResponseEntity<String> payBill(@RequestBody BillPaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        try {
            String res = billPaymentService.payBill(request, username);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<BillPayment>> getBillPayments(@PathVariable Long accountId) {
        return ResponseEntity.ok(billPaymentService.getBillPayments(accountId));
    }
}
