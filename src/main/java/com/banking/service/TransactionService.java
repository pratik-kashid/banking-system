package com.banking.service;

import com.banking.dto.TransferRequest;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transfer(TransferRequest request, String username) {
        if (request.getAmount().signum() <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        Account source = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        // Validate ownership
        if (!source.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own the source account");
        }

        Account destination = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        source.setBalance(source.getBalance().subtract(request.getAmount()));
        destination.setBalance(destination.getBalance().add(request.getAmount()));

        accountRepository.save(source);
        accountRepository.save(destination);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .sourceAccount(source)
                .destinationAccount(destination)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String accountNumber, String username) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
                
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this account");
        }

        return transactionRepository.findBySourceAccount_AccountNumberOrDestinationAccount_AccountNumberOrderByTimestampDesc(
                accountNumber, accountNumber
        );
    }
}
