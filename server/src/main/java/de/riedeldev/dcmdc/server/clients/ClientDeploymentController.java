package de.riedeldev.dcmdc.server.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import de.riedeldev.dcmdc.server.execution.DeploymentExecutor;
import de.riedeldev.dcmdc.server.execution.model.DeploymentExecution.DeploymentState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class ClientDeploymentController {

    private static final int PENDING = 1;
    private static final int SUCCESSFUL = 2;
    private static final int FAILED = 3;

    @Autowired
    private DeploymentExecutor executor;

    @MessageMapping("/api/v1/client/deployment/execution/info")
    public Mono<Void> deploymentExecutionUpdate(DeploymentUpdateInfo info) {
        return Mono.defer(() -> {
            DeploymentState state;
            switch (info.state) {
                case PENDING:
                    state = DeploymentState.Pending;
                    break;
                case SUCCESSFUL:
                    state = DeploymentState.Successful;
                    break;
                case FAILED:
                    state = DeploymentState.Error;
                    break;
                default:
                    state = DeploymentState.Error;
                    info.message = "Unknown Deployment state: " + info.state;
                    log.error("Unknown deployment state id! {}", info.state);
                    break;
            }
            return executor.updateExecutionInfo(info.id, state, info.message);

        });
    }

    @AllArgsConstructor
    @Data
    public static final class DeploymentUpdateInfo {
        Long id;
        Integer state;
        String message;
    }

}
