package de.riedeldev.dcmdc.client.dockeraccess.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.core.model.requests.CreateContainerAnswer;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@Slf4j
public class ContainerFactoryService {

    private Mono<HttpClient> dockerApi;

    private ObjectMapper mapper;

    public ContainerFactoryService() {
    }

    @Autowired
    public void setDockerApi(Mono<HttpClient> dockerApi) {
        this.dockerApi = dockerApi;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Mono<CreateContainerAnswer> createContainer(String containerName, Map<String, Object> containerConfig) {
        return this.dockerApi
                .flatMap(client -> client.post()
                        .uri("/containers/create" + (containerName == null ? "" : "?name=" + containerName))
                        .send(Mono.fromCallable(
                                () -> Unpooled.wrappedBuffer(this.mapper.writeValueAsBytes(containerConfig))))
                        .responseContent().aggregate().asInputStream())
                .map(is -> this.readJsonInput(is, CreateContainerAnswer.class))
                .doOnSubscribe(s -> log.debug("Creating Container {} with config {}", containerName, containerConfig));
    }

    private <T> T readJsonInput(InputStream s, Class<T> clazz) {
        try {
            return this.mapper.readValue(s, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<Void> startContainer(String idOrName) {
        return this.dockerApi.flatMap(client -> client.post().uri("/containers/" + idOrName + "/start")
                .send(Mono.just(Unpooled.EMPTY_BUFFER)).responseSingle((res, data) -> {
                    var status = res.status();
                    if (status.equals(HttpResponseStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.NOT_MODIFIED)) {
                        log.debug("Container was already started... {}", idOrName);
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.BAD_REQUEST)
                            || status.equals(HttpResponseStatus.NOT_FOUND) || status.equals(HttpResponseStatus.CONFLICT)
                            || status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                        return data.asString().doOnNext(message -> {
                            throw new IllegalStateException(message);
                        });

                    } else {
                        throw new IllegalStateException("Unexpected answer: " + status);
                    }
                })).doOnSubscribe(s -> log.debug("Starting Container: {}", idOrName))
                .doOnSuccess(s -> log.debug("Container: {} started", idOrName))
                .doOnError(error -> log.debug("Failed to start container: {}", idOrName)).then();
    }

    public Mono<Void> stopContainer(String idOrName, boolean ignoreNotFound) {
        return this.dockerApi.flatMap(client -> client.post().uri("/containers/" + idOrName + "/stop")
                .send(Mono.just(Unpooled.EMPTY_BUFFER)).responseSingle((res, data) -> {
                    var status = res.status();
                    if (status.equals(HttpResponseStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.NOT_MODIFIED)) {
                        log.debug("Container was already stopped... {}", idOrName);
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.NOT_FOUND)
                            || status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                        if (status.equals(HttpResponseStatus.NOT_FOUND) && ignoreNotFound) {
                            return Mono.empty();
                        }
                        return data.asString().doOnNext(message -> {
                            throw new IllegalStateException(message);
                        });

                    } else {
                        throw new IllegalStateException("Unexpected answer: " + status);
                    }
                })).doOnSubscribe(s -> log.debug("Stopping Container: {}", idOrName))
                .doOnSuccess(s -> log.debug("Container: {} stopped", idOrName))
                .doOnError(error -> log.debug("Failed to stop container: {}", idOrName)).then();

    }

    public Mono<Void> stopContainerIgnoreNotFound(String idOrName) {
        return this.stopContainer(idOrName, true);
    }

    public Mono<Void> restartContainer(String idOrName) {
        return this.dockerApi.flatMap(client -> client.post().uri("/containers/" + idOrName + "/restart")
                .send(Mono.just(Unpooled.EMPTY_BUFFER)).responseSingle((res, data) -> {
                    var status = res.status();
                    if (status.equals(HttpResponseStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.NOT_FOUND)
                            || status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                        return data.asString().doOnNext(message -> {
                            throw new IllegalStateException(message);
                        });

                    } else {
                        throw new IllegalStateException("Unexpected answer: " + status);
                    }
                })).doOnSubscribe(s -> log.debug("Restarting Container: {}", idOrName))
                .doOnSuccess(s -> log.debug("Container: {} restarted", idOrName))
                .doOnError(error -> log.debug("Failed to rsetart container: {}", idOrName)).then();
    }

    public Mono<Void> removeContainer(String idOrName, boolean ignoreNotFound) {
        return this.dockerApi.flatMap(client -> client.delete().uri("/containers/" + idOrName)
                .send(Mono.just(Unpooled.EMPTY_BUFFER)).responseSingle((res, data) -> {
                    var status = res.status();
                    if (status.equals(HttpResponseStatus.NO_CONTENT)) {
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.BAD_REQUEST)
                            || status.equals(HttpResponseStatus.NOT_FOUND) || status.equals(HttpResponseStatus.CONFLICT)
                            || status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                        if (status.equals(HttpResponseStatus.NOT_FOUND) && ignoreNotFound) {
                            return Mono.empty();
                        }
                        return data.asString().doOnNext(message -> {
                            throw new IllegalStateException(message);
                        });

                    } else {
                        throw new IllegalStateException("Unexpected answer: " + status);
                    }
                })).doOnSubscribe(s -> log.debug("Removing Container: {}", idOrName))
                .doOnSuccess(s -> log.debug("Container: {} removed", idOrName))
                .doOnError(error -> log.debug("Failed to remove container: {}", idOrName)).then();
    }

    public Mono<Void> removeContainerIgnoreNotFound(String idOrName) {
        return this.removeContainer(idOrName, true);
    }

    public Mono<Void> pullImage(String image) {
        return this.dockerApi.flatMap(client -> client.post().uri("/images/create?fromImage=" + image)
                .send(Mono.just(Unpooled.EMPTY_BUFFER)).responseSingle((res, data) -> {
                    var status = res.status();
                    if (status.equals(HttpResponseStatus.OK)) {
                        return Mono.empty();
                    } else if (status.equals(HttpResponseStatus.NOT_FOUND)
                            || status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                        return data.asString().doOnNext(message -> {
                            throw new IllegalStateException(message);
                        });

                    } else {
                        throw new IllegalStateException("Unexpected answer: " + status);
                    }
                })).doOnSubscribe(s -> log.debug("Pulling image: {}", image))
                .doOnSuccess(s -> log.debug("Image: {} pulled", image))
                .doOnError(error -> log.debug("Failed to pull image: {}", image)).then();
    }

    @Data
    public static final class MessageAnswer {
        public String message;
    }

}
