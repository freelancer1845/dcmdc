package de.riedeldev.dcmdc.client.dockeraccess.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

public class DockerApiAccessTests {

    @Test
    public void canAccessDockerApi() {
        var access = new DockerApiAccess();
        access.dockerApi = "/var/run/docker.sock";

        var client = access.dockerApiClient().block();
        var mapper = new ObjectMapper();

        var answer = client.get().uri("/containers/json").responseContent().aggregate().asInputStream().map(input -> {
            try {
                return mapper.readValue(input, List.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).block();

        System.out.println(answer);

    }

}
