package de.riedeldev.dcmdc.server.clients;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.server.clients.model.ClientNode;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ClientService {

    private ClientNodeRepository clientNodeRepository;

    @PostConstruct
    protected void postConstruct() {

        var client = new ClientNode();

        client.setApiId("example_api_id_string");
        client.setApiSecret("very-secret-api-secret".getBytes());

        client.setName("Example Client");

        this.clientNodeRepository.save(client);

    }

    public Mono<List<ClientNode>> getClients() {
        return Mono.fromCallable(this.clientNodeRepository::findAll);
    }

    public Mono<Void> createClient(String name, String apiId, byte[] apiSecret) {
        return Mono.fromRunnable(() -> {
            if (this.clientNodeRepository.findByApiId(apiId).isPresent()) {
                throw new IllegalArgumentException("Client with this API id already exists");
            }
            var client = new ClientNode();
            client.setApiId(apiId);
            client.setApiSecret(apiSecret);
            client.setName(name);
            this.clientNodeRepository.save(client);
        });
    }

    @Autowired
    public void setClientNodeRepository(ClientNodeRepository clientNodeRepository) {
        this.clientNodeRepository = clientNodeRepository;
    }
}
