package com.banking.service;

import com.banking.dto.AccountDto;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AccountDto> getUserAccounts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
                
        return user.getAccounts().stream()
                .map(acc -> AccountDto.builder()
                        .accountNumber(acc.getAccountNumber())
                        .balance(acc.getBalance())
                        .build())
                .collect(Collectors.toList());
    }
}
