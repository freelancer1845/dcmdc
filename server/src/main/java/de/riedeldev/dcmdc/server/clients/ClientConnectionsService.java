package de.riedeldev.dcmdc.server.clients;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.server.clients.model.ClientDeploymentExecution;
import de.riedeldev.dcmdc.server.execution.DeploymentExecutionRepository;
import de.riedeldev.dcmdc.server.execution.model.DeploymentExecution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ClientConnectionsService {

    @Autowired
    private DeploymentExecutionRepository executionRepository;
    private Set<ClientHandle> connectedClients = new CopyOnWriteArraySet<>();

    public Mono<Void> tryNotifyForDeployment(DeploymentExecution execution) {
        return Mono.defer(() -> {
            var target = this.connectedClients.stream()
                    .filter(handle -> handle.apiId.equals(execution.getTarget().getApiId())).findFirst();
            if (target.isPresent()) {
                var clientExecution = new ClientDeploymentExecution();
                clientExecution.setConfiguration(execution.getDeployment().getConfiguration());
                clientExecution.setDeploymentId(execution.getDeployment().getId());
                clientExecution.setContainerName(execution.getDeployment().getContainerName());
                clientExecution.setExecutionId(execution.getId());
                clientExecution.setReleaseDeployment(execution.isReleaseDeployment());
                return target.get().notifyForDeployment(clientExecution);

            } else {
                return Mono.empty();
            }
        });
    }

    public Mono<Void> handleConnect(RSocketRequester requester, SetupInfo setup) {
        return Mono.fromRunnable(() -> {
            log.debug("Client '{}' connected.", setup.apiId);
            var handle = new ClientHandle(requester, setup.apiId);
            this.connectedClients.remove(handle);
            this.connectedClients.add(handle);
            this.executionRepository.findAllByClientNodeApiIdWhichArePending(setup.apiId)
                    .forEach(pending -> this.tryNotifyForDeployment(pending).subscribe());
        });
    }

    @AllArgsConstructor
    @EqualsAndHashCode(of = "apiId")
    private static final class ClientHandle {
        final RSocketRequester requester;
        final String apiId;

        Mono<Void> notifyForDeployment(ClientDeploymentExecution execution) {
            log.debug("Notifying client {} for deployment", apiId);
            return this.requester.route("/api/v1/client/deployment/execution").data(execution).retrieveMono(Void.class);
        }
    }

    @Data
    @AllArgsConstructor
    public static final class SetupInfo {
        String apiId;
        byte[] apiSecret;
    }

}
