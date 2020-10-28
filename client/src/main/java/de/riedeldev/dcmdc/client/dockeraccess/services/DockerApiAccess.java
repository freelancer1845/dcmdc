package de.riedeldev.dcmdc.client.dockeraccess.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.codec.http.HttpHeaderNames;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class DockerApiAccess {

    @Value("${docker.api:tcp://localhost:2375}")
    public String dockerApi;

    @Bean
    public Mono<HttpClient> dockerApiClient() {
        return Mono.fromCallable(() -> {
            if (this.dockerApi.startsWith("tcp://")) {
                return HttpClient.create().baseUrl(this.dockerApi)
                        .headers(h -> h.set(HttpHeaderNames.CONTENT_TYPE, "application/json")).compress(true)
                        .followRedirect(true);
            } else {
                return HttpClient.create().remoteAddress(() -> new DomainSocketAddress(this.dockerApi))
                        .headers(h -> h.set(HttpHeaderNames.CONTENT_TYPE, "application/json")).compress(true)
                        .followRedirect(true);
            }

        });
    }

}
