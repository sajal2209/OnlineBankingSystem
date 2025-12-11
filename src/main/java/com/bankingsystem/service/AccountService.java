package com.bankingsystem.service;

import com.bankingsystem.dto.AccountRequest;
import com.bankingsystem.dto.BillPaymentRequest;
import com.bankingsystem.dto.RecurringPaymentRequest;
import com.bankingsystem.dto.TransferRequest;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.entity.Transaction;
import com.bankingsystem.entity.User;
import com.bankingsystem.enums.Frequency;
import com.bankingsystem.enums.PaymentStatus;
import com.bankingsystem.enums.PaymentType;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.BillPaymentRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BillPaymentRepository billPaymentRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository,
                          TransactionRepository transactionRepository, BillPaymentRepository billPaymentRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.billPaymentRepository = billPaymentRepository;
    }

    public List<Account> getAccountsByUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account createAccount(AccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setBalance(request.getBalance());
        account.setUser(user);

        return accountRepository.save(account);
    }

    @Transactional
    public ResponseEntity<String> transferFunds(TransferRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long loggedInUserId = user.getId();

        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (!fromAccount.getUser().getId().equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only transfer from your own account.");
        }

        if (!fromAccount.getUser().isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is deactivated");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType("TRANSFER");

        if (request.getAmount().compareTo(BigDecimal.valueOf(100000)) > 0) {
            transaction.setStatus("PENDING");
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Transaction requires banker approval");
        }

        // Normal transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transaction.setStatus("COMPLETED");
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Transfer successful");
    }

    public List<Map<String, Object>> getUserTransactions(Long userId) {
        List<Transaction> transactions = transactionRepository.findByFromAccount_User_IdOrToAccount_User_Id(userId, userId);

        return transactions.stream().map(tx -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", tx.getId());
            map.put("type", tx.getType());
            map.put("amount", tx.getAmount());
            map.put("status", tx.getStatus());
            map.put("timestamp", tx.getTimestamp());
            map.put("fromAccount", tx.getFromAccount().getAccountNumber());
            map.put("fromUser", tx.getFromAccount().getUser().getUsername());
            map.put("toAccount", tx.getToAccount() != null ? tx.getToAccount().getAccountNumber() : "N/A");
            map.put("toUser", tx.getToAccount() != null ? tx.getToAccount().getUser().getUsername() : "Bill Payment");

            if (tx.getToAccount() != null && tx.getFromAccount().getUser().getId().equals(tx.getToAccount().getUser().getId())) {
                map.put("transferType", "Self Transfer");
            } else if (tx.getToAccount() != null) {
                map.put("transferType", "Transfer to Another User");
            } else {
                map.put("transferType", "Bill Payment");
            }

            if (tx.getFromAccount().getUser().getId().equals(userId)) {
                map.put("direction", "Sent to " + (tx.getToAccount() != null ? tx.getToAccount().getUser().getUsername() : "Bill Payment"));
            } else {
                map.put("direction", "Received from " + tx.getFromAccount().getUser().getUsername());
            }

            return map;
        }).collect(Collectors.toList());
    }

    public List<Transaction> getTransactions(Long userId, String type, String status, String startDate, String endDate) {
        List<Transaction> transactions = transactionRepository.findAll();

        if (userId != null) {
            transactions = transactions.stream()
                    .filter(tx -> tx.getFromAccount().getUser().getId().equals(userId)
                            || (tx.getToAccount() != null && tx.getToAccount().getUser().getId().equals(userId)))
                    .collect(Collectors.toList());
        }
        if (type != null) {
            String t = type.toUpperCase();
            transactions = transactions.stream().filter(tx -> tx.getType().equalsIgnoreCase(t)).collect(Collectors.toList());
        }
        if (status != null) {
            transactions = transactions.stream().filter(tx -> tx.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
        }
        if (startDate != null && endDate != null) {
            LocalDateTime start = java.time.LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = java.time.LocalDate.parse(endDate).atTime(23, 59, 59);
            transactions = transactions.stream()
                    .filter(tx -> !tx.getTimestamp().isBefore(start) && !tx.getTimestamp().isAfter(end))
                    .collect(Collectors.toList());
        }

        return transactions;
    }

    @Transactional
    public ResponseEntity<String> payBill(BillPaymentRequest request, String username) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Ensure ownership
        if (!account.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only pay bills from your own account.");
        }

        if (!account.getUser().isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is deactivated");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        BillPayment payment = new BillPayment();
        payment.setAccount(account);
        payment.setBillerName(request.getBillerName());
        payment.setAmount(request.getAmount());
        payment.setPaymentType(PaymentType.IMMEDIATE);
        payment.setStatus(PaymentStatus.COMPLETED);
        billPaymentRepository.save(payment);

        // Record transaction for bill payment
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account);
        transaction.setToAccount(null);
        transaction.setAmount(request.getAmount());
        transaction.setStatus("COMPLETED");
        transaction.setType("BILL_PAYMENT");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Bill payment successful");
    }

    @Transactional
    public ResponseEntity<String> scheduleRecurringPayment(RecurringPaymentRequest request, String username) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only schedule payments for your own account.");
        }

        BillPayment payment = new BillPayment();
        payment.setAccount(account);
        payment.setBillerName(request.getBillerName());
        payment.setAmount(request.getAmount());
        payment.setPaymentType(PaymentType.RECURRING);
        payment.setFrequency(Frequency.valueOf(request.getFrequency().toUpperCase()));

        // next payment date logic
        Frequency freq = payment.getFrequency();
        LocalDateTime now = LocalDateTime.now();
        switch (freq) {
            case DAILY:
                payment.setNextPaymentDate(now.plusDays(1));
                break;
            case WEEKLY:
                payment.setNextPaymentDate(now.plusWeeks(1));
                break;
            case MONTHLY:
                payment.setNextPaymentDate(now.plusMonths(1));
                break;
            default:
                payment.setNextPaymentDate(now.plusDays(1));
        }

        payment.setStatus(PaymentStatus.PENDING);
        billPaymentRepository.save(payment);

        return ResponseEntity.ok("Recurring payment scheduled successfully");
    }

    public List<BillPayment> getScheduledPayments(Long userId) {
        return billPaymentRepository.findByAccount_User_Id(userId);
    }

    @Transactional
    public ResponseEntity<String> cancelScheduledPayment(Long id) {
        BillPayment payment = billPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.CANCELLED);
        billPaymentRepository.save(payment);
        return ResponseEntity.ok("Scheduled payment cancelled");
    }
}
