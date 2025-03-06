package ru.ciklon.bank.bankcoreservice.api.dto;

import java.util.List;
import java.util.UUID;

public class AllClientInfoResponseDto {
    private UUID clientId;
    private List<AccountDto> accounts;
    private List<AccountTransactionDto> accountTransactions;
    private List<CreditContractDto> creditContracts;
    private List<CreditTransactionDto> creditTransactions;
}
