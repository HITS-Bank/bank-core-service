package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountDto {
    private UUID accountId;
    private String accountNumber;
    private BigDecimal balance;
    private boolean blocked;
    private boolean closed;
}
