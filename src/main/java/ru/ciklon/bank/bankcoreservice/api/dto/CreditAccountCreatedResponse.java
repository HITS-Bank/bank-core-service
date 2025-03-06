package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreditAccountCreatedResponse {
    private UUID creditId;
    private UUID accountId;
    private BigDecimal creditAmount;
}
