package com.bankingsystem.repository;
import com.bankingsystem.entity.BillPayment;
import com.bankingsystem.enums.PaymentStatus;
import com.bankingsystem.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
//    List<BillPayment> findByUserId(Long userId);


    @Query("SELECT b FROM BillPayment b WHERE b.account.user.id = :userId")
    List<BillPayment> findByUserId(@Param("userId") Long userId);



        List<BillPayment> findByAccountId(Long accountId);


        List<BillPayment> findByAccount_User_Id(Long userId);

        List<BillPayment> findByStatus(String status);



    List<BillPayment> findByPaymentTypeAndStatus(PaymentType paymentType, PaymentStatus status);





}
