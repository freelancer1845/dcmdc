package de.riedeldev.dcmdc.client.dockeraccess.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DockerApiAccessTests {

    @Test
    public void canAccessDockerApi() {
        var access = new DockerApiAccess();
        access.dockerApi = "tcp://localhost:2375";

        var client = access.dockerApiClient();

        var container = client.get().uri("/containers/json").exchange().flatMap(ans -> ans.bodyToMono(List.class))
                .block();
        assertTrue(container.size() > 0);

    }

}
