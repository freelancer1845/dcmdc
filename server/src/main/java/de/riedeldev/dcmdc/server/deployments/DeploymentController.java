package de.riedeldev.dcmdc.server.deployments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.riedeldev.dcmdc.server.deployments.model.Deployment;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class DeploymentController {

    @Autowired
    private DeploymentService service;

    @GetMapping("/api/v1/deployments")
    public Mono<List<Deployment>> getAll() {
        return this.service.getAll();
    }

    @GetMapping("/api/v1/deployments/{id}")
    public Mono<Deployment> getActualById(@PathVariable("id") Long id) {
        return this.service.getActualById(id);
    }

    @PatchMapping("/api/v1/deployments")
    public Mono<Deployment> patchDeployment(@RequestBody Deployment deployment) {
        return this.service.patchDeployment(deployment);
    }

    @PostMapping("/api/v1/deployments")
    public Mono<Deployment> createDeployment(@RequestBody Deployment deployment) {
        return this.service.createDeployment(deployment);
    }

    @DeleteMapping("/api/v1/deployments/{id}")
    public Mono<Void> deleteDeploymentById(@PathVariable Long id) {
        return this.service.deleteDeploymentById(id);
    }

}
