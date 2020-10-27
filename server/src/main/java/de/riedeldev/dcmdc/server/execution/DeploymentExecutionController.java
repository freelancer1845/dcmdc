package de.riedeldev.dcmdc.server.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.riedeldev.dcmdc.server.execution.model.DeploymentExecution;
import lombok.Data;
import reactor.core.publisher.Mono;

@RestController
public class DeploymentExecutionController {

    @Autowired
    private DeploymentExecutionRepository repository;

    @PostMapping("/api/v1/deployments/executions/{id}")
    public Mono<Page<DeploymentExecution>> getPageForDeployment(@PathVariable Long id,
            @RequestBody ApiPageRequest request) {
        return Mono.fromCallable(() -> this.repository.findAllByDeploymentId(id,
                PageRequest.of(request.page, request.pageSize, Sort.by(Order.desc("id")))));
    }

    @Data
    public static final class ApiPageRequest {
        Integer page;
        Integer pageSize;
    }

}
