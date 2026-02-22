package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.dto.TransactionRequest;
import com.banking.dto.TransferRequest;
import com.banking.entity.Transaction;
import com.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Transaction>> deposit(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            Transaction transaction = transactionService.deposit(request, username);
            return ResponseEntity.ok(
                    ApiResponse.success("Deposit successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Transaction>> withdraw(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            Transaction transaction = transactionService.withdraw(request, username);
            return ResponseEntity.ok(
                    ApiResponse.success("Withdrawal successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Transaction[]>> transfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            Transaction[] transactions = transactionService.transfer(request, username);
            return ResponseEntity.ok(
                    ApiResponse.success("Transfer successful", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactions(
            @PathVariable String accountNumber,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            List<Transaction> transactions =
                    transactionService.getAccountTransactions(accountNumber, username);
            return ResponseEntity.ok(
                    ApiResponse.success("Transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}