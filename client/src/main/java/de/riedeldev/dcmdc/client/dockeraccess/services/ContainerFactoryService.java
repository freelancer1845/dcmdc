package de.riedeldev.dcmdc.client.dockeraccess.services;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.riedeldev.dcmdc.core.model.requests.CreateContainerAnswer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContainerFactoryService {

    private final WebClient dockerApi;

    public ContainerFactoryService(WebClient dockerApi) {
        this.dockerApi = dockerApi;
    }

    public Mono<CreateContainerAnswer> createContainer(String containerName, Map<String, Object> containerConfig) {
        return dockerApi.post().uri("/containers/create" + (containerName == null ? "" : "?name=" + containerName))
                .bodyValue(containerConfig).exchange().flatMap(res -> res.bodyToMono(CreateContainerAnswer.class));
    }

    public Mono<Void> startContainer(String idOrName) {
        return dockerApi.delete().uri("/containers/{id}/start", idOrName).exchange().flatMap(res -> {
            if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                return Mono.empty();
            } else if (res.statusCode().equals(HttpStatus.NOT_MODIFIED)) {
                log.debug("Container was already started... {}", idOrName);
                return Mono.empty();
            } else if (res.statusCode().equals(HttpStatus.BAD_REQUEST) || res.statusCode().equals(HttpStatus.NOT_FOUND)
                    || res.statusCode().equals(HttpStatus.CONFLICT)
                    || res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return res.bodyToMono(MessageAnswer.class).doOnNext(ans -> {
                    throw new IllegalArgumentException(ans.message);
                }).then();
            } else {
                throw new IllegalStateException("Unexpected answer: " + res.statusCode());
            }
        });
    }

    public Mono<Void> stopContainer(String idOrName) {
        return dockerApi.post().uri("/containers/{id}/stop", idOrName).exchange().flatMap(res -> {
            if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                return Mono.empty();
            } else if (res.statusCode().equals(HttpStatus.NOT_MODIFIED)) {
                log.debug("Container was already stopped... {}", idOrName);
                return Mono.empty();
            } else if (res.statusCode().equals(HttpStatus.NOT_FOUND)) {
                return Mono.error(new IllegalArgumentException(
                        String.format("Container with idOrName: '{}' not found.'", idOrName)));
            } else if (res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return Mono.error(new IllegalArgumentException("Internal Server Error"));
            } else {
                throw new IllegalStateException("Unexpected answer: " + res.statusCode());
            }
        });
    }

    public Mono<Void> restartContainer(String idOrName) {
        return dockerApi.post().uri("/containers/{id}/restart", idOrName).exchange().flatMap(res -> {
            if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                return Mono.empty();
            } else if (res.statusCode().equals(HttpStatus.NOT_FOUND)
                    || res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return res.bodyToMono(MessageAnswer.class).doOnNext(ans -> {
                    throw new IllegalArgumentException(ans.message);
                }).then();
            } else {
                throw new IllegalStateException("Unexpected answer: " + res.statusCode());
            }
        });
    }

    public Mono<Void> removeContainer(String idOrName) {
        return dockerApi.delete().uri("/containers/{id}", idOrName).exchange().flatMap(res -> {
            if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                return Mono.empty();
            } else if (res.statusCode().equals(HttpStatus.BAD_REQUEST) || res.statusCode().equals(HttpStatus.NOT_FOUND)
                    || res.statusCode().equals(HttpStatus.CONFLICT)
                    || res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return res.bodyToMono(MessageAnswer.class).doOnNext(ans -> {
                    throw new IllegalArgumentException(ans.message);
                }).then();
            } else {
                throw new IllegalStateException("Unexpected answer: " + res.statusCode());
            }
        });
    }

    @Data
    public static final class MessageAnswer {
        public String message;
    }

}
