package de.riedeldev.dcmdc.server.deployments;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.server.deployments.model.Deployment;
import de.riedeldev.dcmdc.server.deployments.model.DeploymentId;
import de.riedeldev.dcmdc.server.execution.DeploymentExecutor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DeploymentService {

    private DeploymentRepository repository;

    private DeploymentExecutor executor;

    @PostConstruct
    protected void postConstruct() {
        var d = new Deployment();
        d.setName("Example-Deployment");
        d.setConfiguration(
                "{\"Image\":\"nginx:latest\"}"
                        .getBytes(Charset.forName("UTF-8")));
        d.setContainerName("nginx-from-dcmdc");
        d.setCreationTimestamp(Instant.now());
        this.repository.save(d);
    }

    public Mono<List<Deployment>> getAll() {
        return Mono.fromCallable(this.repository::findAll);
    }

    public Mono<Deployment> getActualById(Long id) {
        return Mono.fromCallable(() -> this.repository.findNewestById(id));
    }

    public Mono<Deployment> createDeployment(Deployment toCreate) {
        return Mono.fromCallable(() -> {
            toCreate.setCreationTimestamp(Instant.now());
            toCreate.setConfiguration("{\"Image\": \"hello-world:latest\"}".getBytes(Charset.forName("UTF-8")));
            return this.repository.save(toCreate);
        });
    }

    public Mono<Void> deleteDeploymentById(Long id) {
        return Mono.fromRunnable(() -> {
            var currentDeployment = this.repository.findNewestById(id);
            var deployments = this.repository.findAllById(id);

            currentDeployment.getTargetNodes().forEach(node -> {
                this.executor.releaseDeployment(currentDeployment, node).subscribe();
            });
            this.repository.deleteAll(deployments);
        });
    }

    public Mono<Deployment> patchDeployment(Deployment modified) {
        return Mono.fromCallable(() -> {
            var currentO = this.repository.findById(new DeploymentId(modified.id, modified.getCreationTimestamp()));
            var saved = this.repository.save(modified);
            if (currentO.isPresent()) {
                var current = currentO.get();
                current.getTargetNodes().forEach(node -> {
                    if (modified.getTargetNodes().contains(node) == false) {
                        this.executor.releaseDeployment(saved, node).subscribe();
                    }
                });
            }
            this.executor.submitDeployment(saved);
            return saved;
        });
    }

    @Autowired
    public void setRepository(DeploymentRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setExecutor(DeploymentExecutor executor) {
        this.executor = executor;
    }

}
