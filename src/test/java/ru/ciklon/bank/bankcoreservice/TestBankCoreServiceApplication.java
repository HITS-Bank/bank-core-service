package ru.ciklon.bank.bankcoreservice;

import org.springframework.boot.SpringApplication;

public class TestBankCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(BankCoreServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
