package ru.ciklon.bank.bankcoreservice.api.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ciklon.bank.bankcoreservice.api.constant.ApiConstants;
import ru.ciklon.bank.bankcoreservice.api.dto.ClientInfoDto;
import ru.ciklon.bank.bankcoreservice.core.service.ClientService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController(ApiConstants.CLIENTS_BASE)
public class ClientController {

    private final ClientService clientService;

    @GetMapping(ApiConstants.CLIENT_INFO)
    public ResponseEntity<ClientInfoDto> getClientInfo(@PathVariable("clientId") final UUID clientId, @RequestParam final UUID employeeId) {
        return ResponseEntity.ok(clientService.getClientInfo(clientId, employeeId));
    }
}
