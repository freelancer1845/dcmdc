package de.riedeldev.dcmdc.server.deployments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.riedeldev.dcmdc.server.deployments.model.Deployment;
import de.riedeldev.dcmdc.server.deployments.model.DeploymentId;

public interface DeploymentRepository extends JpaRepository<Deployment, DeploymentId> {

    @Query("select d from Deployment d WHERE d.id = :id order by d.creationTimestamp DESC")
    public Deployment findNewestById(Long id);

    @Query("select d from Deployment d where d.id = :id")
    public List<Deployment> findAllById(Long id);

}
