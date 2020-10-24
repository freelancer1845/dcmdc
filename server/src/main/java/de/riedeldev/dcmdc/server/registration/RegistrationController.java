package de.riedeldev.dcmdc.server.registration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.riedeldev.dcmdc.core.model.ClientNode;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private RegistrationService service;

    @GetMapping("api/v1/client/register")
    public Mono<ClientNode> register() {
        return this.service.register();
    }

    @GetMapping("api/v1/client/register/list")
    public Mono<List<ClientNode>> getAll() {
        return this.service.getClients();
    }


}
