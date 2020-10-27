package de.riedeldev.dcmdc.server.clients;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.riedeldev.dcmdc.server.clients.model.ClientNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping("/api/v1/clients")
    public Mono<List<ClientNode>> getClientNodes() {
        return this.service.getClients();
    }

    @PostMapping("/api/v1/clients")
    public Mono<Void> createClient(@RequestBody CreateClientRequest request) {
        return this.service.createClient(request.name, request.apiId, request.apiSecret.getBytes(Charset.forName("UTF-8")));
    }

    @Data
    public static final class CreateClientRequest {
        String name;
        String apiId;
        String apiSecret;
    }

}
