package de.riedeldev.dcmdc.server.execution;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.riedeldev.dcmdc.server.execution.model.DeploymentExecution;

public interface DeploymentExecutionRepository extends JpaRepository<DeploymentExecution, Long> {

    public Page<DeploymentExecution> findAllByDeploymentId(Long id, Pageable page);

    @Query("SELECT e FROM DeploymentExecution e WHERE e.target.apiId = :apiId AND (e.state = 0 OR e.state = 1)")
    public List<DeploymentExecution> findAllByClientNodeApiIdWhichArePending(String apiId);

}
