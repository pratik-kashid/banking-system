package com.banking.repository;

import com.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccount_IdOrDestinationAccount_IdOrderByTimestampDesc(Long sourceId, Long destinationId);
    List<Transaction> findBySourceAccount_AccountNumberOrDestinationAccount_AccountNumberOrderByTimestampDesc(String sourceAccount, String destinationAccount);
}
