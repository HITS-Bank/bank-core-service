package ru.ciklon.bank.bankcoreservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Core Service API",
                version = "v1",
                description = "Документация для Core Service, который управляет счетами, транзакциями и кредитами"
        )
)
public class OpenApiConfig {
}
