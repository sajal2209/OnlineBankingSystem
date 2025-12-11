
package com.bankingsystem.repository;

import com.bankingsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Fetch all pending transactions
    List<Transaction> findByStatus(String status);

    // Fetch transactions by account involvement
    List<Transaction> findByFromAccount_Id(Long accountId);
    List<Transaction> findByToAccount_Id(Long accountId);

    // Fetch transactions where account is either sender or receiver
    List<Transaction> findByFromAccount_IdOrToAccount_Id(Long fromId, Long toId);

    List<Transaction> findByFromAccount_User_IdOrToAccount_User_Id(Long fromUserId, Long toUserId);
}
