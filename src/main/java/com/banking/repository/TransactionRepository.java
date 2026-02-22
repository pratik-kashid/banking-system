package com.banking.repository;

import com.banking.entity.Account;
import com.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountOrderByTimestampDesc(Account account);

    List<Transaction> findByAccountAndTimestampBetweenOrderByTimestampDesc(
            Account account, LocalDateTime start, LocalDateTime end);

    List<Transaction> findByTransactionId(String transactionId);
}