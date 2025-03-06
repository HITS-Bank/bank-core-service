package ru.ciklon.bank.bankcoreservice.core.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ciklon.bank.bankcoreservice.api.dto.AccountDto;
import ru.ciklon.bank.bankcoreservice.api.dto.AccountTransactionDto;
import ru.ciklon.bank.bankcoreservice.api.dto.CreditRepaymentRequest;
import ru.ciklon.bank.bankcoreservice.api.dto.OpenAccountDto;
import ru.ciklon.bank.bankcoreservice.api.dto.TransactionRequest;
import ru.ciklon.bank.bankcoreservice.api.enums.AccountTransactionType;
import ru.ciklon.bank.bankcoreservice.api.enums.CreditTransactionType;
import ru.ciklon.bank.bankcoreservice.core.entity.Account;
import ru.ciklon.bank.bankcoreservice.core.entity.AccountTransaction;
import ru.ciklon.bank.bankcoreservice.core.entity.Client;
import ru.ciklon.bank.bankcoreservice.core.entity.CreditContract;
import ru.ciklon.bank.bankcoreservice.core.entity.CreditTransaction;
import ru.ciklon.bank.bankcoreservice.core.mapper.AccountMapper;
import ru.ciklon.bank.bankcoreservice.core.mapper.ClientMapper;
import ru.ciklon.bank.bankcoreservice.core.mapper.AccountTransactionMapper;
import ru.ciklon.bank.bankcoreservice.core.mapper.CreditContractMapper;
import ru.ciklon.bank.bankcoreservice.core.repository.AccountRepository;
import ru.ciklon.bank.bankcoreservice.core.repository.ClientRepository;
import ru.ciklon.bank.bankcoreservice.core.repository.CreditContractRepository;
import ru.ciklon.bank.bankcoreservice.core.repository.AccountTransactionRepository;
import ru.ciklon.bank.bankcoreservice.core.repository.CreditTransactionRepository;
import ru.ciklon.bank.bankcoreservice.core.utils.AccountNumberGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CreditContractRepository creditContractRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final CreditTransactionRepository creditTransactionRepository;
    private final ClientRepository clientRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountTransactionMapper accountTransactionMapper;
    private final AccountMapper accountMapper;


    public AccountDto openAccount(final OpenAccountDto openAccountDto) {
        final String generatedAccountNumber = accountNumberGenerator.generateAccountNumber();
        final Client client = clientRepository.findById(openAccountDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        final Account account = new Account(client, generatedAccountNumber);
        accountRepository.save(account);
        return accountMapper.map(account);
    }

    public void closeAccount(final UUID accountId) {
        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setClosed(true);
        accountRepository.save(account);
    }

    public AccountDto deposit(final TransactionRequest request) {
        final Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.isClosed()) {
            throw new RuntimeException("Account is closed");
        }
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);
        recordAccountTransaction(account, AccountTransactionType.DEPOSIT, request.getAmount());
        return accountMapper.map(account);
    }

    public AccountDto withdraw(final TransactionRequest request) {
        final Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.isClosed()) {
            throw new RuntimeException("Account is closed");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        accountRepository.save(account);
        recordAccountTransaction(account, AccountTransactionType.WITHDRAW, request.getAmount());
        return accountMapper.map(account);
    }

    public void blockAccount(final UUID clientId) {
        final List<Account> accounts = accountRepository.findByClientId(clientId);
        accounts.forEach(account -> account.setBlocked(true));
        accountRepository.saveAll(accounts);
    }

    public void unblockAccount(final UUID clientId) {
        final List<Account> accounts = accountRepository.findByClientId(clientId);
        accounts.forEach(account -> account.setBlocked(false));
        accountRepository.saveAll(accounts);
    }

    public List<AccountTransactionDto> getAccountHistory(final UUID accountId) {
        return accountTransactionRepository.findByAccountId(accountId).stream()
                .map(accountTransactionMapper::map)
                .toList();
    }

    public List<AccountDto> getAccountsByClientId(final UUID clientId) {
        return accountRepository.findByClientId(clientId).stream()
                .map(accountMapper::map)
                .toList();
    }

    public List<AccountTransactionDto> getAccountTransactionsByClientId(final UUID clientId) {
        return accountRepository.findByClientId(clientId).stream()
                .flatMap(account -> accountTransactionRepository.findByAccountId(account.getId()).stream())
                .map(accountTransactionMapper::map)
                .toList();
    }


    /**
     * Частичное погашение кредита по запросу (например, от клиента).
     * Списываем указанную сумму с баланса счёта и уменьшаем остаток по кредитному договору.
     */
    public CreditPaymentResponseDTO repayCredit(final CreditRepaymentRequest repaymentRequest) {
        if (repaymentRequest.getCreditAmount() == null || repaymentRequest.getCreditAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Repayment amount must be greater than zero");
        }

        final CreditContract creditContract = creditContractRepository.findById(repaymentRequest.getCreditContractId())
                .orElseThrow(() -> new RuntimeException("Credit contract not found"));

        final Account account = accountRepository.findById(creditContract.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.isClosed()) {
            throw new IllegalStateException("Account is closed");
        }

        if (account.getBalance().subtract(repaymentRequest.getCreditAmount()).compareTo(repaymentRequest.getCreditAmount()) < 0) {
            throw new IllegalStateException("Insufficient funds in account for repayment");
        }

        account.setBalance(account.getBalance().subtract(repaymentRequest.getCreditAmount()));
        accountRepository.save(account);
        recordCreditTransaction(creditContract, CreditTransactionType.CREDIT_REPAYMENT_AUTO, repaymentRequest.getCreditAmount());

        creditContract.setRemainingAmount(creditContract.getRemainingAmount().max(BigDecimal.ZERO));
        creditContractRepository.save(creditContract);
    }

    private void recordAccountTransaction(final Account account, final AccountTransactionType type, final BigDecimal amount) {
        final AccountTransaction tx = new AccountTransaction();

        tx.setAccount(account);
        tx.setTransactionType(type);
        tx.setAmount(amount);
        tx.setTransactionDate(LocalDateTime.now());

        accountTransactionRepository.save(tx);
    }

    private void recordCreditTransaction(final CreditContract creditContract, final CreditTransactionType type, final BigDecimal amount) {
        final CreditTransaction tx = new CreditTransaction();

        tx.setCreditContract(creditContract);
        tx.setTransactionType(type);
        tx.setPaymentAmount(amount);
        tx.setPaymentDate(LocalDateTime.now());

        creditTransactionRepository.save(tx);
    }
}
