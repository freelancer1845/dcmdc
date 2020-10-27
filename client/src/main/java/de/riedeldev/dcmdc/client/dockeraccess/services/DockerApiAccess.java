package de.riedeldev.dcmdc.client.dockeraccess.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.unix.DomainSocketAddress;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class DockerApiAccess {

    @Value("${docker.api:tcp://localhost:2375}")
    public String dockerApi;

    @Bean
    public Mono<WebClient> dockerApiClient() {
        return Mono.fromCallable(() -> {
            if (this.dockerApi.startsWith("tcp://")) {
                return WebClient.builder().baseUrl(this.dockerApi).build();
            } else {
                HttpClient client = HttpClient.create().remoteAddress(() -> new DomainSocketAddress(this.dockerApi));
                return WebClient.builder().clientConnector(new ReactorClientHttpConnector(client)).baseUrl("http:/").build();
            }

        });
    }

}
