package de.riedeldev.dcmdc.server.execution;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.server.clients.ClientConnectionsService;
import de.riedeldev.dcmdc.server.clients.model.ClientNode;
import de.riedeldev.dcmdc.server.deployments.model.Deployment;
import de.riedeldev.dcmdc.server.execution.model.DeploymentExecution;
import de.riedeldev.dcmdc.server.execution.model.DeploymentExecution.DeploymentState;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DeploymentExecutor {

    @Autowired
    private ClientConnectionsService clientService;

    private DeploymentExecutionRepository repository;

    private DirectProcessor<Deployment> deploymentsProcessor = DirectProcessor.create();
    private FluxSink<Deployment> deploymentsSink = deploymentsProcessor.sink();

    public void submitDeployment(Deployment deployment) {
        this.deploymentsSink.next(deployment);
    }

    public Mono<Void> updateExecutionInfo(Long executionId, DeploymentState state, String message) {
        return Mono.fromRunnable(() -> {
            var execution = this.repository.findById(executionId).orElseThrow();
            execution.setState(state);
            if (message != null) {
                execution.setErrorDescription(message);
            }
            this.repository.save(execution);
        });
    }

    public Mono<Void> releaseDeployment(Deployment deployment, ClientNode node) {
        return Mono.fromCallable(() -> {
            var execution = new DeploymentExecution();
            execution.setDeployment(deployment);
            execution.setTarget(node);
            execution.setState(DeploymentState.WaitingToBePickedUp);
            execution.setReleaseDeployment(true);
            return execution;
        }).map(this.repository::save).flatMap(this.clientService::tryNotifyForDeployment);
    }

    @PostConstruct
    protected void postConstruct() {
        this.setupDeploymentHandler();
    }

    private void setupDeploymentHandler() {
        this.deploymentsProcessor.map(deployment -> {
            List<DeploymentExecution> executions = new ArrayList<>();
            deployment.getTargetNodes().forEach(node -> {
                var execution = new DeploymentExecution();
                execution.setDeployment(deployment);
                execution.setTarget(node);
                execution.setState(DeploymentState.WaitingToBePickedUp);
                executions.add(execution);
            });
            return executions;
        }).map(this.repository::saveAll).flatMap(Flux::fromIterable).flatMap(this.clientService::tryNotifyForDeployment)
                .subscribe();
    }

    @Autowired
    public void setRepository(DeploymentExecutionRepository repository) {
        this.repository = repository;
    }

}
