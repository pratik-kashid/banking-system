package com.banking.service;

import com.banking.dto.TransactionRequest;
import com.banking.dto.TransferRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction deposit(TransactionRequest request, String username) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Verify the account belongs to the user
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        if (!account.getActive()) {
            throw new RuntimeException("Account is not active");
        }

        // Update account balance
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(account.getBalance());
        transaction.setAccount(account);
        transaction.setDescription(request.getDescription() != null ?
                request.getDescription() : "Deposit");
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(TransactionRequest request, String username) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Verify the account belongs to the user
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        if (!account.getActive()) {
            throw new RuntimeException("Account is not active");
        }

        // Verify PIN
        if (request.getPin() == null || !request.getPin().equals(account.getPin())) {
            throw new RuntimeException("Invalid PIN. Please enter the correct PIN.");
        }

        // Check sufficient balance
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Update account balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(account.getBalance());
        transaction.setAccount(account);
        transaction.setDescription(request.getDescription() != null ?
                request.getDescription() : "Withdrawal");
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction[] transfer(TransferRequest request, String username) {
        // Get source account
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        // Verify the source account belongs to the user
        if (!fromAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to source account");
        }

        // Verify PIN
        if (request.getPin() == null || !request.getPin().equals(fromAccount.getPin())) {
            throw new RuntimeException("Invalid PIN. Please enter the correct PIN.");
        }

        // Get destination account
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (!fromAccount.getActive() || !toAccount.getActive()) {
            throw new RuntimeException("One or both accounts are not active");
        }

        // Check if accounts are different
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance in source account");
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create transaction records
        Transaction debitTransaction = new Transaction();
        debitTransaction.setType(Transaction.TransactionType.TRANSFER_OUT);
        debitTransaction.setAmount(request.getAmount());
        debitTransaction.setBalanceAfter(fromAccount.getBalance());
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setRelatedAccountNumber(toAccount.getAccountNumber());
        debitTransaction.setDescription(request.getDescription() != null ?
                request.getDescription() :
                "Transfer to " + toAccount.getAccountNumber());
        debitTransaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setType(Transaction.TransactionType.TRANSFER_IN);
        creditTransaction.setAmount(request.getAmount());
        creditTransaction.setBalanceAfter(toAccount.getBalance());
        creditTransaction.setAccount(toAccount);
        creditTransaction.setRelatedAccountNumber(fromAccount.getAccountNumber());
        creditTransaction.setDescription(request.getDescription() != null ?
                request.getDescription() :
                "Transfer from " + fromAccount.getAccountNumber());
        creditTransaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return new Transaction[]{debitTransaction, creditTransaction};
    }

    public List<Transaction> getAccountTransactions(String accountNumber, String username) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Verify the account belongs to the user
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        return transactionRepository.findByAccountOrderByTimestampDesc(account);
    }
}