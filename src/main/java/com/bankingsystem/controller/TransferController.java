package com.bankingsystem.controller;

import com.bankingsystem.service.TransferService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    private final TransferService transferService;
    public TransferController(TransferService transferService) { this.transferService = transferService; }

    @PostMapping
    public Map<String, String> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam double amount) {
        transferService.transferFunds(fromAccountId, toAccountId, amount);
        return Map.of("status", "success");
    }
}
