package de.riedeldev.dcmdc.server.clients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.riedeldev.dcmdc.core.model.ClientNode;
import de.riedeldev.dcmdc.core.model.ClientStatus;
import de.riedeldev.dcmdc.server.registration.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ClientController {

    @Autowired
    private RegistrationService service;

    @Autowired
    private ClientService clientService;

    @MessageMapping("/api/v1/register/rsocket")
    public Mono<Void> registerRSocket(String uuid, RSocketRequester requester) {
        return Mono.fromRunnable(() -> {
            this.service.setRSocketClient(uuid, requester);
        });
    }

    @MessageMapping("/api/v1/client/push/info")
    public Mono<Void> receiveState(ClientNode nodeInfo, RSocketRequester requester) {
        log.info("Received new node info: " + nodeInfo);
        return this.service.patchClientNode(nodeInfo, requester);
    }

    @GetMapping("/api/v1/clients")
    public Mono<List<ClientNode>> uiClients() {
        return this.service.getClients();
    }

    @GetMapping("/api/v1/clients/status")
    public Flux<ClientStatus> clientStatus(@RequestParam("uuid") String uuid) {
        return this.clientService.clientStatus(uuid).take(1); // TODO : replace by event handling
    }

}
