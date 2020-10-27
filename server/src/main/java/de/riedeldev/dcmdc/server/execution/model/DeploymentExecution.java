package de.riedeldev.dcmdc.server.execution.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import de.riedeldev.dcmdc.server.clients.model.ClientNode;
import de.riedeldev.dcmdc.server.deployments.model.Deployment;
import lombok.Data;

@Entity
@Data
public class DeploymentExecution {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Deployment deployment;

    @OneToOne
    private ClientNode target;

    private DeploymentState state;

    private String errorDescription;

    public enum DeploymentState {
        WaitingToBePickedUp, Pending, Successful, Error;
    }

    private boolean releaseDeployment = false;

}
