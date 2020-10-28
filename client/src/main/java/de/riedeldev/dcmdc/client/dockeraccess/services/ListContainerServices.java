package de.riedeldev.dcmdc.client.dockeraccess.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.riedeldev.dcmdc.core.model.DockerContainerFlyweight;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class ListContainerServices {
    private final Mono<HttpClient> dockerAccess;

    private ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public ListContainerServices(Mono<HttpClient> dockerAccess) {
        this.dockerAccess = dockerAccess;
    }

    public Mono<List<DockerContainerFlyweight>> listRunningContainers() {
        return this.dockerAccess.flatMap(client -> client.get().uri("/containers/json").responseContent().aggregate()
                .asInputStream().map(this::mapContainerList));

    }

    public Mono<List<DockerContainerFlyweight>> listAllContainers() {
        return this.dockerAccess.flatMap(client -> client.get().uri("/containers/json?all=true").responseContent()
                .aggregate().asInputStream().map(this::mapContainerList));
    }

    @SuppressWarnings("unchecked")
    private List<DockerContainerFlyweight> mapContainerList(InputStream input) {
        try {
            return (List<DockerContainerFlyweight>) this.mapper.readValue(input, List.class).stream()
                    .map(info -> new DockerContainerFlyweight((Map<String, Object>) info)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
