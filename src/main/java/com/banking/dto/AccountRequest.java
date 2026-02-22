package com.banking.dto;

import com.banking.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotBlank(message = "Account number is required")
    @Size(min = 10, max = 16, message = "Account number must be between 10-16 digits")
    private String accountNumber;

    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;

    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 6, message = "PIN must be 4-6 digits")
    private String pin;

    private BigDecimal initialDeposit = BigDecimal.ZERO;
}