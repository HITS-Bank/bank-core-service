package ru.ciklon.bank.bankcoreservice.config.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ciklon.bank.bankcoreservice.api.dto.ClientInfoDto;
import ru.ciklon.bank.bankcoreservice.api.dto.CreditAccountCreatedResponse;
import ru.ciklon.bank.bankcoreservice.core.entity.Account;
import ru.ciklon.bank.bankcoreservice.core.entity.CreditContract;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(final String topic, final String message) {
        kafkaTemplate.send(topic, message);
    }

    public void sendUserInfoForCredit(final ClientInfoDto clientInfoDto) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String message = objectMapper.writeValueAsString(clientInfoDto);
            kafkaTemplate.send("credit.client.info.response", message);
            log.info("Sent USER_INFO_FOR_CREDIT event: {}", message);
        } catch (final Exception e) {
            log.error("Error sending USER_INFO_FOR_CREDIT event", e);
        }
    }

    public void sendCreditAccountCreatedEvent(final CreditContract creditContract, final Account creditAccount) {
        try {
            final CreditAccountCreatedResponse event = new CreditAccountCreatedResponse(
                    creditContract.getCreditContractId(),
                    creditAccount.getId(),
                    creditContract.getCreditAmount()
            );
            final ObjectMapper objectMapper = new ObjectMapper();
            final String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("credit-account-created", message);
            log.info("Sent CREDIT_ACCOUNT_CREATED event: {}", event);
        } catch (final Exception e) {
            log.error("Error sending CREDIT_ACCOUNT_CREATED event", e);
        }
    }
}
