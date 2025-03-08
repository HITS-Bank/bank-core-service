package ru.ciklon.bank.bankcoreservice.api.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ciklon.bank.bankcoreservice.api.constant.ApiConstants;
import ru.ciklon.bank.bankcoreservice.api.dto.ClientInfoDto;
import ru.ciklon.bank.bankcoreservice.core.service.ClientService;
import ru.ciklon.bank.bankcoreservice.core.service.EmployeeService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.EMPLOYEES_BASE)
public class EmployeeController {

    private final ClientService clientService;

    @PostMapping(ApiConstants.BLOCK_CLIENT_ACCOUNTS)
    public ResponseEntity<Void> blockClientAccounts(@PathVariable("clientId") final UUID clientId) {
        clientService.blockClientAccounts(clientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiConstants.UNBLOCK_CLIENT_ACCOUNTS)
    public ResponseEntity<Void> unblockClientAccounts(@PathVariable("clientId") final UUID clientId) {
        clientService.unblockClientAccounts(clientId);
        return ResponseEntity.ok().build();
    }
}
