package de.riedeldev.dcmdc.client.deployments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class DeploymentsController {

    @Autowired
    private DeploymentExecutor executor;

    @MessageMapping("/api/v1/client/deployment/execution")
    public Mono<Void> handleDeploymentNotification(DeploymentRequest request) {
        log.debug("Received Direct Deployment Request: {}", request);
        return this.executor.scheduleDeployment(request);
    }

}
