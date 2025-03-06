package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreditRepaymentRequest {
    private UUID creditContractId;
    private BigDecimal creditAmount;
    private LocalDateTime enrollmentDate;
}
