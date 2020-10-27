package de.riedeldev.dcmdc.server.deployments.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;

import de.riedeldev.dcmdc.server.clients.model.ClientNode;
import lombok.Data;

@Data
@Entity
@IdClass(DeploymentId.class)
public class Deployment {

    @Id
    @GeneratedValue
    public Long id;

    @Id
    private Instant creationTimestamp;

    private String name;
    private String containerName;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ClientNode> targetNodes;

    @Lob
    private byte[] configuration;

}
