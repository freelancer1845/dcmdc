package de.riedeldev.dcmdc.server.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import de.riedeldev.dcmdc.server.clients.ClientConnectionsService.SetupInfo;
import de.riedeldev.dcmdc.server.execution.DeploymentExecutionRepository;
import reactor.core.publisher.Mono;

@Controller
public class ClientConnectionController {

    @Autowired
    private ClientConnectionsService service;

    @Autowired
    private DeploymentExecutionRepository executionRepository;

    @ConnectMapping
    public Mono<Void> handleConnect(RSocketRequester requester, SetupInfo setup) {
        return this.service.handleConnect(requester, setup);
    }

    @MessageMapping("/api/v1/client/deployments/open")
    public Mono<Object> getOpenDeploymentExecutions(String apiId) {
        return Mono.fromCallable(() -> {
            return executionRepository.findAllByClientNodeApiIdWhichArePending(apiId);
        });
    }

}
