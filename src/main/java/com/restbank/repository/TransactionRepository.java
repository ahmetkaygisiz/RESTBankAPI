package com.restbank.repository;

import com.restbank.domain.Transaction;
import com.restbank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Transactional
    @Query("SELECT t FROM Transaction t WHERE t.transferFrom = :accountNumber or t.transferTo = :accountNumber")
    List<Transaction> getTransactionsByAccountNumber(@Param("accountNumber") String accountNumber);

}
