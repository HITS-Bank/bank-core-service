package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {
    private final UUID accountId;
    private final BigDecimal amount;
}
