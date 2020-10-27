package de.riedeldev.dcmdc.server.clients.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import de.riedeldev.dcmdc.server.deployments.model.Deployment;
import lombok.Data;

@Entity
@Data
public class ClientNode {

    @Id
    private String apiId;

    private byte[] apiSecret;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_deployments")
    private Set<Deployment> deployments = new HashSet<>();

}
