package de.riedeldev.dcmdc.server.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.core.model.ClientNode;
import de.riedeldev.dcmdc.server.clients.ClientService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RegistrationService {

    private List<ClientNode> clientNodes = new ArrayList<>();

    private Map<String, RSocketRequester> clients = new HashMap<>();

    @Autowired
    private ClientService clientService;

    public Mono<ClientNode> register() {
        return Mono.fromCallable(() -> {
            var client = new ClientNode();

            client.setUuid(UUID.randomUUID().toString());
            this.clientNodes.add(client);
            return client;
        });
    }

    public Mono<List<ClientNode>> getClients() {
        return Mono.just(clientNodes);
    }

    public Mono<Void> patchClientNode(ClientNode node, RSocketRequester requester) {
        return Mono.fromRunnable(() -> {
            this.clientNodes.removeIf(n -> n.getUuid().equals(node.getUuid()));
            this.clientNodes.add(node);
            setRSocketClient(node.getUuid(), requester);
        });
    }

    public void setRSocketClient(String uuid, RSocketRequester requester) {
        this.clients.put(uuid, requester);
        log.info(String.format("Client with uuid '%s' successfully established rsocket connection.", uuid));
        this.clientService.handleNewRSocketClient(uuid, requester);
    }
}
