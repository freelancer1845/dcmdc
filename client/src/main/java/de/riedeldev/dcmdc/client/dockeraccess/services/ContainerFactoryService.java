package de.riedeldev.dcmdc.client.dockeraccess.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContainerFactoryService {

    private final WebClient dockerApi;

    public ContainerFactoryService(WebClient dockerApi) {
        this.dockerApi = dockerApi;
    }

    @SuppressWarnings("unchecked")
    public Mono<List<String>> createContainer(String containerName, Map<String, Object> containerConfig) {
        return dockerApi.post().uri("/containers/create" + (containerName == null ? "" : "?name=" + containerName))
                .bodyValue(containerConfig).exchange().flatMap(res -> res.bodyToMono(Map.class)).map(data -> {
                    log.info(data.toString());

                    List<String> result = new ArrayList<>();
                    result.add((String) data.get("Id"));
                    if (data.containsKey("Warnings")) {
                        result.addAll((Collection<String>) data.get("Warnings"));
                    }
                    return result;
                });
    }

}
