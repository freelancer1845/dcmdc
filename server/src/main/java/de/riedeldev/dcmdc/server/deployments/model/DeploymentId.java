package de.riedeldev.dcmdc.server.deployments.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

import javax.persistence.GeneratedValue;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeploymentId implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = 6302615962610954126L;
    private Long id;
    private Instant creationTimestamp;
}
