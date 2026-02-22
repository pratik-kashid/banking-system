package com.banking.controller;

import com.banking.dto.AccountRequest;
import com.banking.dto.ApiResponse;
import com.banking.entity.Account;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<Account>> createAccount(
            @Valid @RequestBody AccountRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            Account account = accountService.createAccount(username, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Account created successfully", account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Account>>> getUserAccounts(
            Authentication authentication) {
        try {
            String username = authentication.getName();
            List<Account> accounts = accountService.getUserAccounts(username);
            return ResponseEntity.ok(
                    ApiResponse.success("Accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<Account>> getAccount(
            @PathVariable String accountNumber,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            Account account = accountService.getAccountByNumber(accountNumber, username);
            return ResponseEntity.ok(
                    ApiResponse.success("Account retrieved successfully", account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<String>> deleteAccount(
            @PathVariable String accountNumber,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            String result = accountService.deleteAccount(accountNumber, username);
            return ResponseEntity.ok(ApiResponse.success(result, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}