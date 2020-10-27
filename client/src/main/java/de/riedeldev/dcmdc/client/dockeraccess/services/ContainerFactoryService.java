package de.riedeldev.dcmdc.client.dockeraccess.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

    private Mono<WebClient> dockerApi;

    public ContainerFactoryService() {
    }

    @Autowired
    public void setDockerApi(Mono<WebClient> dockerApi) {
        this.dockerApi = dockerApi;
    }

    public Mono<CreateContainerAnswer> createContainer(String containerName, Map<String, Object> containerConfig) {
        return dockerApi
                .flatMap(client -> client.post()
                        .uri("/containers/create" + (containerName == null ? "" : "?name=" + containerName))
                        .bodyValue(containerConfig).exchange())
                .flatMap(res -> res.bodyToMono(CreateContainerAnswer.class)).doOnSubscribe(s -> {
                    log.debug("Creating Container {} with config {}", containerName, containerConfig);
                });
    }

    public Mono<Void> startContainer(String idOrName) {
        return dockerApi
                .flatMap(client -> client.post().uri("/containers/{id}/start", idOrName).bodyValue("").exchange())
                .flatMap(res -> {
                    if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (res.statusCode().equals(HttpStatus.NOT_MODIFIED)) {
                        log.debug("Container was already started... {}", idOrName);
                        return Mono.empty();
                    } else if (res.statusCode().equals(HttpStatus.BAD_REQUEST)
                            || res.statusCode().equals(HttpStatus.NOT_FOUND)
                            || res.statusCode().equals(HttpStatus.CONFLICT)
                            || res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        return res.bodyToMono(String.class).doOnNext(ans -> {
                            throw new IllegalArgumentException(ans);
                        }).then();
                    } else {
                        throw new IllegalStateException("Unexpected answer: " + res.statusCode());
                    }
                });
    }

    public Mono<Void> stopContainer(String idOrName, boolean ignoreNotFound) {
        return dockerApi.flatMap(client -> client.post().uri("/containers/{id}/stop", idOrName).exchange())
                .flatMap(res -> {
                    if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (res.statusCode().equals(HttpStatus.NOT_MODIFIED)) {
                        log.debug("Container was already stopped... {}", idOrName);
                        return Mono.empty();
                    } else if (res.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        if (ignoreNotFound == true) {
                            return Mono.empty();
                        }
                        return Mono.error(new IllegalArgumentException(
                                String.format("Container with idOrName: '%s' not found.'", idOrName)));
                    } else if (res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        return Mono.error(new IllegalArgumentException("Internal Server Error"));
                    } else {
                        throw new IllegalStateException("Unexpected answer: " + res.statusCode());
                    }
                });
    }

    public Mono<Void> stopContainerIgnoreNotFound(String idOrName) {
        return this.stopContainer(idOrName, true);
    }

    public Mono<Void> restartContainer(String idOrName) {
        return dockerApi.flatMap(client -> client.post().uri("/containers/{id}/restart", idOrName).exchange())
                .flatMap(res -> {
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

    public Mono<Void> removeContainer(String idOrName, boolean ignoreNotFound) {
        return dockerApi.flatMap(client -> client.delete().uri("/containers/{id}", idOrName).exchange())
                .flatMap(res -> {
                    if (res.statusCode().equals(HttpStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (res.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        if (ignoreNotFound) {
                            return Mono.empty();
                        }
                        return res.bodyToMono(MessageAnswer.class).doOnNext(ans -> {
                            throw new IllegalArgumentException(ans.message);
                        }).then();
                    } else if (res.statusCode().equals(HttpStatus.BAD_REQUEST)
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

    public Mono<Void> removeContainerIgnoreNotFound(String idOrName) {
        return this.removeContainer(idOrName, true);
    }

    public Mono<Void> pullImage(String image) {
        return this.dockerApi
                .flatMap(
                        client -> client.post().uri("/images/create?fromImage={image}", image).bodyValue("").exchange())
                .flatMap(res -> {
                    if (res.statusCode().equals(HttpStatus.OK)) {
                        return Mono.empty();
                    } else if (res.statusCode().equals(HttpStatus.NOT_FOUND)
                            || res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        return res.bodyToMono(MessageAnswer.class).doOnNext(ans -> {
                            throw new IllegalArgumentException(ans.message);
                        }).then();
                    } else {
                        throw new IllegalStateException("Unexpected answer: " + res.statusCode());
                    }
                }).doOnSubscribe(s -> log.debug("Pulling Image {}", image));
    }

    @Data
    public static final class MessageAnswer {
        public String message;
    }

}
