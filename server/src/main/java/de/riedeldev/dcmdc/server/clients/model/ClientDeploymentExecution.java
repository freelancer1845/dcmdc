package de.riedeldev.dcmdc.server.clients.model;

import lombok.Data;

@Data
public class ClientDeploymentExecution {

    public Long executionId;
    public byte[] configuration;
    public Long deploymentId;
    public String containerName;
    public Boolean releaseDeployment = Boolean.FALSE;

}
