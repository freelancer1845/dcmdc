package de.riedeldev.dcmdc.client.dockeraccess.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ListContainerServiceTests {

    private DockerApiAccess access;
    private ListContainerServices service;

    @BeforeEach
    public void beforeEach() {
        access = new DockerApiAccess();
        access.dockerApi = "/var/run/docker.sock";
        this.service = new ListContainerServices(access.dockerApiClient());
        this.service.setMapper(new ObjectMapper());

    }

    @Test
    public void listsContainers() {
        var list = this.service.listRunningContainers().block();
        assertTrue(list.size() > 0);
    }

    @Test
    public void listsAllContainers() {
        var list = this.service.listAllContainers().block();
        assertTrue(list.size() > 5);
    }

}
