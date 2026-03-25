package com.banking.controller;

import com.banking.dto.AccountDto;
import com.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountDto>> getMyAccounts(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(accountService.getUserAccounts(username));
    }
}
