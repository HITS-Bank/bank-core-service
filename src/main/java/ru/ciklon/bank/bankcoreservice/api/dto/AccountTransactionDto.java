package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.Data;
import ru.ciklon.bank.bankcoreservice.api.enums.AccountTransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AccountTransactionDto {
    private UUID accountTransactionId;
    private UUID accountId;
    private AccountTransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
}
