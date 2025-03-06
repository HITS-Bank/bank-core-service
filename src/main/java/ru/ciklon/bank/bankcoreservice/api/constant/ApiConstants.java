package ru.ciklon.bank.bankcoreservice.api.constant;

public class ApiConstants {

    // Версия API
    public static final String API_VERSION = "/api/v1";

    // Базовые пути контроллеров
    public static final String ACCOUNTS_BASE = API_VERSION + "/accounts";
    public static final String LOANS_BASE = API_VERSION + "/loans";
    public static final String EMPLOYEES_BASE = API_VERSION + "/employees";
    public static final String CLIENTS_BASE = API_VERSION + "/clients";
    public static final String CREDIT_RATES_BASE = API_VERSION + "/credit-rates";

    // Эндпоинты для работы со счетами
    // для клиентов
    public static final String CREATE_ACCOUNT = "/create"; // POST /accounts/create
    public static final String CLOSE_ACCOUNT = "/{accountId}/close"; // POST /accounts/{accountId}/close

    // для сотрудников
    public static final String BLOCK_CLIENT_ACCOUNTS = "/{clientId}/block"; // POST /accounts/{clientId}/block
    public static final String UNBLOCK_CLIENT_ACCOUNTS = "/{clientId}/unblock"; // POST /accounts/{clientId}/unblock
    public static final String CLIENT_INFO = "/{clientId}/info"; // GET /accounts/{clientId}/info

    // Эндпоинты для работы с транзакциями
    public static final String DEPOSIT = "/deposit"; // POST /accounts/deposit
    public static final String WITHDRAW = "/withdraw"; // POST /accounts/withdraw
    public static final String ACCOUNT_HISTORY = "/{accountId}/history"; // GET /accounts/{accountId}/history

    // Эндпоинты для работы с кредитами
    public static final String REPAY_LOAN = "/{loanId}/repay"; // POST /loans/{loanId}/repay

    // Эндпоинты для сотрудников

    public static final String CREATE_CREDIT_TARIFF = "/credit-tariff/create"; // POST /credit-tariff/create
    public static final String CREATE_CREDIT_RATE = "/credit-rates/create"; // POST /credit-rates/create


    public static final String CLIENT_CREDITS = "/client/{clientId}/credits"; // GET /clients/{clientId}/credits


}
