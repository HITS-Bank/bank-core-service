package ru.ciklon.bank.bankcoreservice.api.dto;

import lombok.Data;


@Data
public class CreditPaymentResponseDTO {
    private boolean isApproved;
    private String approvedAmount;
}
