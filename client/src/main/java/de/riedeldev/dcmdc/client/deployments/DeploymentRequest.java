package de.riedeldev.dcmdc.client.deployments;

import lombok.Data;

@Data
public class DeploymentRequest {
    public Long executionId;
    public byte[] configuration;
    public Long deploymentId;
    public String containerName;
    public Boolean releaseDeployment = Boolean.FALSE;
}
