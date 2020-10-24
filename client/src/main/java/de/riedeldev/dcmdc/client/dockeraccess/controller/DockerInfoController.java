package de.riedeldev.dcmdc.client.dockeraccess.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import de.riedeldev.dcmdc.client.dockeraccess.services.ContainerFactoryService;
import de.riedeldev.dcmdc.client.dockeraccess.services.ListContainerServices;
import de.riedeldev.dcmdc.core.model.DockerContainerFlyweight;
import de.riedeldev.dcmdc.core.model.requests.CreateContainerAnswer;
import reactor.core.publisher.Mono;

@Controller
public class DockerInfoController {

    @Autowired
    private ListContainerServices listService;

    @Autowired
    private ContainerFactoryService containerFactory;

    @MessageMapping("/api/v1/client/containers")
    public Mono<List<DockerContainerFlyweight>> getAllContainers() {
        return listService.listAllContainers();
    }

    @MessageMapping("/api/v1/client/containers/create")
    public Mono<CreateContainerAnswer> createContainer(CreateContainerRequest request) {
        return this.containerFactory.createContainer(request.name, request.configuration);
    }

    @MessageMapping("/api/v1/client/containers/stop")
    public Mono<Void> stopContainer(String idOrName) {
        return this.containerFactory.stopContainer(idOrName);
    }

    @MessageMapping("/api/v1/client/containers/remove")
    public Mono<Void> removeContainer(String idOrName) {
        return this.containerFactory.removeContainer(idOrName);
    }

    @MessageMapping("/api/v1/client/containers/restart")
    public Mono<Void> restartContainer(String idOrName) {
        return this.containerFactory.restartContainer(idOrName);
    }

    @MessageMapping("/api/v1/client/containers/start")
    public Mono<Void> startContainer(String idOrName) {
        return this.containerFactory.startContainer(idOrName);
    }

    public static final class CreateContainerRequest {
        String name = null;
        Map<String, Object> configuration;
    }
}
