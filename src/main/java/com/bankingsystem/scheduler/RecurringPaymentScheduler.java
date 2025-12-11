package com.bankingsystem.scheduler;


import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.enums.Frequency;
import com.bankingsystem.enums.PaymentStatus;
import com.bankingsystem.enums.PaymentType;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.BillPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecurringPaymentScheduler {

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every midnight
    public void processRecurringPayments() {
        List<BillPayment> payments = billPaymentRepository.findByPaymentTypeAndStatus(PaymentType.RECURRING, PaymentStatus.PENDING);
        for (BillPayment payment : payments) {
            if (payment.getNextPaymentDate().isBefore(LocalDateTime.now())) {
                Account account = payment.getAccount();
                if (account.getBalance().compareTo(payment.getAmount()) >= 0) {
                    account.setBalance(account.getBalance().subtract(payment.getAmount()));
                    accountRepository.save(account);
                    payment.setStatus(PaymentStatus.COMPLETED);
                    payment.setNextPaymentDate(calculateNextDate(payment.getFrequency()));
                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                }
                billPaymentRepository.save(payment);
            }
        }
    }

    private LocalDateTime calculateNextDate(Frequency frequency) {
        return switch (frequency) {
            case DAILY -> LocalDateTime.now().plusDays(1);
            case WEEKLY -> LocalDateTime.now().plusWeeks(1);
            case MONTHLY -> LocalDateTime.now().plusMonths(1);
        };
    }
}
