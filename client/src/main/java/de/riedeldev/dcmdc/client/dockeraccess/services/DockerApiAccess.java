package de.riedeldev.dcmdc.client.dockeraccess.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DockerApiAccess {

    @Value("${docker.api:tcp://localhost:2375}")
    public String dockerApi;

    @Bean
    public WebClient dockerApiClient() {
        return WebClient.builder().baseUrl(this.dockerApi).build();
    }

}
