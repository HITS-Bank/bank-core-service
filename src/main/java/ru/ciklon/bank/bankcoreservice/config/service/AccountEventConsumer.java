package ru.ciklon.bank.bankcoreservice.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ciklon.bank.bankcoreservice.api.dto.AccountDto;
import ru.ciklon.bank.bankcoreservice.api.dto.ClientInfoDto;
import ru.ciklon.bank.bankcoreservice.api.dto.CreditApprovedDto;
import ru.ciklon.bank.bankcoreservice.api.dto.CreditRepaymentRequest;
import ru.ciklon.bank.bankcoreservice.api.dto.OpenAccountDto;
import ru.ciklon.bank.bankcoreservice.core.service.AccountService;
import ru.ciklon.bank.bankcoreservice.core.service.ClientService;
import ru.ciklon.bank.bankcoreservice.core.service.CreditService;
import ru.ciklon.bank.bankcoreservice.core.service.EmployeeService;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final AccountService accountService;
    private final CreditService creditService;
    private final EmployeeService employeeService;
    private final KafkaProducerService kafkaProducerService;
    private final ClientService clientService;

    @KafkaListener(topics = "create-account", groupId = "bank-group")
    public void handleCreateAccount(final String message) {
        log.info("Received create-account event: {}", message);
        try {
            final OpenAccountDto openAccountDto = parseMessage(message, OpenAccountDto.class);
            final AccountDto createdAccount = accountService.openAccount(openAccountDto);
            log.info("Account created successfully: {}", createdAccount);
        } catch (Exception e) {
            log.error("Error processing create-account event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "close-account", groupId = "bank-group")
    public void handleCloseAccount(final String message) {
        log.info("Received close-account event: {}", message);
        try {
            final UUID accountId = UUID.fromString(message);
            accountService.closeAccount(accountId);
            log.info("Account closed successfully: {}", accountId);
        } catch (Exception e) {
            log.error("Error processing close-account event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "credit-approved", groupId = "bank-group")
    public void handleCreditApproved(final String message) {
        log.info("Received credit-create event: {}", message);
        try {
            final CreditApprovedDto creditApprovedDto = parseMessage(message, CreditApprovedDto.class);
            creditService.processCreditApproval(creditApprovedDto);
            log.info("Credit created successfully for client {}", creditApprovedDto.getClientId());
        } catch (Exception e) {
            log.error("Error processing credit-create event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "credit-repayment", groupId = "bank-group")
    public void handleCreditRepayment(final String message) {
        log.info("Received credit repayment event: {}", message);
        try {
            final CreditRepaymentRequest repaymentRequest = parseMessage(message, CreditRepaymentRequest.class);
            accountService.repayCredit(repaymentRequest);
            log.info("Credit repayment processed for application {}", repaymentRequest.getCreditContractId());
        } catch (Exception e) {
            log.error("Error processing credit repayment event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "block-account", groupId = "bank-group")
    public void handleBlockAccount(final String message) {
        log.info("Received block-account event: {}", message);
        try {
            final UUID clientId = UUID.fromString(message);
            accountService.blockAccount(clientId);
            log.info("Accounts blocked for client {}", clientId);
        } catch (Exception e) {
            log.error("Error processing block-account event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "unblock-account", groupId = "bank-group")
    public void handleUnblockAccount(final String message) {
        log.info("Received unblock-account event: {}", message);
        try {
            final UUID clientId = UUID.fromString(message);
            accountService.unblockAccount(clientId);
            log.info("Accounts unblocked for client {}", clientId);
        } catch (Exception e) {
            log.error("Error processing unblock-account event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "block-employee", groupId = "bank-group")
    public void handleBlockEmployee(final String message) {
        log.info("Received block-employee event: {}", message);
        try {
            final UUID employeeId = UUID.fromString(message);
            employeeService.blockEmployee(employeeId);
            log.info("Employee blocked {}", employeeId);
        } catch (Exception e) {
            log.error("Error processing block-employee event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "unblock-employee", groupId = "bank-group")
    public void handleUnblockEmployee(final String message) {
        log.info("Received unblock-employee event: {}", message);
        try {
            final UUID employeeId = UUID.fromString(message);
            employeeService.unblockEmployee(employeeId);
            log.info("Employee unblocked {}", employeeId);
        } catch (Exception e) {
            log.error("Error processing unblock-employee event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "credit.client.info.request", groupId = "bank-group")
    public void handleClientInfoRequest(final String message) {
        log.info("Received client info request: {}", message);
        try {
            final UUID clientId = UUID.fromString(message);
            final ClientInfoDto clientInfo = clientService.getClientInfoForCredit(clientId);
            kafkaProducerService.sendUserInfoForCredit(clientInfo);
            log.info("Client info sent for client {}", clientId);
        } catch (Exception e) {
            log.error("Error processing client info request: {}", e.getMessage(), e);
        }
    }


    private <T> T parseMessage(final String message, final Class<T> clazz) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, clazz);
    }
}
