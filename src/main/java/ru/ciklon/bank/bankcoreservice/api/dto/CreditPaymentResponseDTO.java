package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditPaymentResponseDTO {
    private boolean isApproved;
    private BigDecimal approvedAmount;
}
