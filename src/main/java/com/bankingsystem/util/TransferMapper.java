
package com.bankingsystem.util;

import com.bankingsystem.dto.TransferRequest;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.Transaction;

import java.time.LocalDateTime;

public class TransferMapper {

    public static Transaction toEntity(TransferRequest request, Account fromAccount, Account toAccount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setType("TRANSFER");
        transaction.setStatus("PENDING");
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}

