package com.banking.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class AccountDto {
    private String accountNumber;
    private BigDecimal balance;
}
