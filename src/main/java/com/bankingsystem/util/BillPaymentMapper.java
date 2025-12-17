
package com.bankingsystem.util;

import com.bankingsystem.dto.BillPaymentRequest;
import com.bankingsystem.entity.Account;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.enums.PaymentStatus;

public class BillPaymentMapper {

    public static BillPayment toEntity(BillPaymentRequest request, Account account) {
        BillPayment billPayment = new BillPayment();
        billPayment.setAccount(account);
        billPayment.setBillerName(request.getBillerName());
        billPayment.setAmount(request.getAmount());
        billPayment.setStatus(PaymentStatus.PENDING);
        return billPayment;
    }
}

