package de.riedeldev.dcmdc.client.dockeraccess.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.riedeldev.dcmdc.core.model.DockerContainerFlyweight;
import reactor.core.publisher.Mono;

@Service
public class ListContainerServices {
    private final Mono<WebClient> dockerAccess;

    @Autowired
    public ListContainerServices(Mono<WebClient> dockerAccess) {
        this.dockerAccess = dockerAccess;
    }

    public Mono<List<DockerContainerFlyweight>> listRunningContainers() {
        return dockerAccess.flatMap(client -> client.get().uri("/containers/json").exchange())
                .flatMap(resp -> resp.bodyToMono(List.class)).map(this::mapContainerList);
    }

    public Mono<List<DockerContainerFlyweight>> listAllContainers() {
        return dockerAccess.flatMap(client -> client.get().uri("/containers/json?all=true").exchange())
                .flatMap(resp -> resp.bodyToMono(List.class)).map(this::mapContainerList);
    }

    @SuppressWarnings("unchecked")
    private List<DockerContainerFlyweight> mapContainerList(List<?> data) {
        return (List<DockerContainerFlyweight>) data.stream()
                .map(info -> new DockerContainerFlyweight((Map<String, Object>) info)).collect(Collectors.toList());
    }

}
