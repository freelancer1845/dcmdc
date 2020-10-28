package de.riedeldev.dcmdc.client.deployments;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.client.dockeraccess.services.ContainerFactoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

@Service
@Slf4j
public class DeploymentExecutor {

    public static final int PENDING = 1;
    public static final int SUCCESSFUL = 2;
    public static final int FAILED = 3;

    @Autowired
    private Mono<RSocketRequester> requester;

    @Autowired
    private ContainerFactoryService containerService;

    private ConcurrentLinkedQueue<DeploymentRequest> scheduledRequests = new ConcurrentLinkedQueue<>();

    private UnicastProcessor<DeploymentRequest> scheduleProcessor = UnicastProcessor.create(scheduledRequests);
    private FluxSink<DeploymentRequest> scheduleSink = scheduleProcessor.sink();

    public Mono<Void> scheduleDeployment(DeploymentRequest request) {
        return Mono.fromRunnable(() -> {
            this.scheduleSink.next(request);
        });
    }

    @PostConstruct
    protected void postConstruct() {
        var type = new ParameterizedTypeReference<List<DeploymentRequest>>() {

        };
        requester.flatMap(req -> req.route("/api/v1/client/deployments/open").retrieveMono(type))
                .flatMapMany(requests -> Flux.fromIterable(requests))
                .flatMap(request -> this.scheduleDeployment(request)).subscribe();

        this.scheduleProcessor.doOnNext(deployment -> {
            log.debug("Executing Deployment: {}", deployment);
            this.requester.flatMap(req -> req.route("/api/v1/client/deployment/execution/info")
                    .data(new DeploymentUpdateInfo(deployment.executionId, PENDING, null)).send()).subscribe();
        }).concatMap(this::executeDeployment).onErrorContinue((error, deployment) -> {

        }).subscribe();
    }

    @AllArgsConstructor
    @Data
    public static final class DeploymentUpdateInfo {
        Long id;
        Integer state;
        String message;
    }

    private Mono<Void> executeDeployment(DeploymentRequest deploymentRequest) {
        return Mono.defer(() -> {
            if (deploymentRequest.releaseDeployment) {
                return this.containerService.stopContainerIgnoreNotFound(deploymentRequest.containerName)
                        .then(this.containerService.removeContainerIgnoreNotFound(deploymentRequest.containerName));
            }
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, Object>> ref = new TypeReference<Map<String, Object>>() {
            };
            try {
                var config = mapper.readValue(deploymentRequest.configuration, ref);
                if (deploymentRequest.containerName == null) {
                    deploymentRequest.containerName = generateContainerName(deploymentRequest);
                }
                return this.containerService.stopContainerIgnoreNotFound(deploymentRequest.containerName)
                        .then(this.containerService.removeContainerIgnoreNotFound(deploymentRequest.containerName))
                        .then(this.containerService.pullImage((String) config.get("Image")))
                        .then(this.containerService.createContainer(deploymentRequest.containerName, config))
                        .then(this.containerService.startContainer(deploymentRequest.containerName)).then();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).doOnSuccess(s -> {
            log.debug("Executed Deployment: {}", deploymentRequest);
            this.requester
                    .flatMap(req -> req.route("/api/v1/client/deployment/execution/info")
                            .data(new DeploymentUpdateInfo(deploymentRequest.executionId, SUCCESSFUL, null)).send())
                    .subscribe();
        }).doOnError(error -> {
            log.error("Failed to executed deployment {}", deploymentRequest);
            log.debug("Attached Throwable", error);
            this.requester.flatMap(req -> req.route("/api/v1/client/deployment/execution/info")
                    .data(new DeploymentUpdateInfo(deploymentRequest.executionId, FAILED, error.getMessage())).send())
                    .subscribe();
        });

    }

    private String generateContainerName(DeploymentRequest request) {
        return "dcmdc-deployment-" + request.deploymentId;
    }
}
