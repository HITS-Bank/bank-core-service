package ru.ciklon.bank.bankcoreservice.api.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ciklon.bank.bankcoreservice.api.constant.ApiConstants;
import ru.ciklon.bank.bankcoreservice.api.dto.AccountTransactionDto;
import ru.ciklon.bank.bankcoreservice.api.dto.OpenAccountDto;
import ru.ciklon.bank.bankcoreservice.api.dto.TransactionRequest;
import ru.ciklon.bank.bankcoreservice.core.service.AccountService;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController(ApiConstants.ACCOUNTS_BASE)
public class AccountController {

    private final AccountService accountService;

    @PostMapping(ApiConstants.CREATE_ACCOUNT)
    public ResponseEntity<Void> createAccount(@RequestBody final OpenAccountDto openAccountDto) {
        accountService.openAccount(openAccountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(ApiConstants.CLOSE_ACCOUNT)
    public ResponseEntity<Void> closeAccount(@PathVariable final UUID accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiConstants.DEPOSIT)
    public ResponseEntity<Void> deposit(@RequestBody final TransactionRequest transactionRequest) {
        accountService.deposit(transactionRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiConstants.WITHDRAW)
    public ResponseEntity<Void> withdraw(@RequestBody final TransactionRequest transactionRequest) {
        accountService.withdraw(transactionRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiConstants.BLOCK_CLIENT_ACCOUNTS)
    public ResponseEntity<Void> blockAccount(@PathVariable final UUID clientId) {
        accountService.blockAccount(clientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiConstants.UNBLOCK_CLIENT_ACCOUNTS)
    public ResponseEntity<Void> unblockAccount(@PathVariable final UUID clientId) {
        accountService.unblockAccount(clientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiConstants.ACCOUNT_HISTORY)
    public ResponseEntity<List<AccountTransactionDto>> getAccountHistory(@PathVariable final UUID accountId) {
        return ResponseEntity.ok(accountService.getAccountHistory(accountId));
    }

}
