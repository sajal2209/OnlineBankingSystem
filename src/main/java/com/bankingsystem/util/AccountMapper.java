
package com.bankingsystem.util;

import com.bankingsystem.dto.AccountRequest;
import com.bankingsystem.dto.AccountResponse;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.User;

public class AccountMapper {

    public static Account toEntity(AccountRequest request, User user) {
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setType(request.getType());
        account.setBalance(request.getBalance());
        account.setUser(user);
        return account;
    }

    public static AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setType(account.getType());
        response.setBalance(account.getBalance());
        return response;
    }
}

