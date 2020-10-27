package de.riedeldev.dcmdc.server.clients;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.riedeldev.dcmdc.server.clients.model.ClientNode;

public interface ClientNodeRepository extends JpaRepository<ClientNode, Long> {

    public Optional<ClientNode> findByApiId(String apiId);
}
