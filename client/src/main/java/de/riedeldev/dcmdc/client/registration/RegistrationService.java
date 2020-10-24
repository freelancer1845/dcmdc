package de.riedeldev.dcmdc.client.registration;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.riedeldev.dcmdc.client.ClientApplication;
import de.riedeldev.dcmdc.core.model.ClientNode;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistrationService {

    @Autowired
    private RSocketRequester requester;

    @PostConstruct
    protected void postConstruct() {
    }

    public static String boostrapRegistration(String host) {
        var client = WebClient.builder().baseUrl(host + "/api/v1/client/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", host + "/api/v1/client/")).build();

        var registerReq = client.get().uri("register");
        var node = registerReq.exchange().flatMap(ans -> ans.bodyToMono(ClientNode.class)).block();
        log.info("Successfully registered as client node.");
        log.info("Node UUID: " + node.getUuid());
        return node.getUuid();
    }

    public void publishState() {
        var clientNode = new ClientNode();
        clientNode.setUuid(ClientApplication.clientUuid);
        clientNode.setName("Test Application");
        clientNode.setDescription("Test Application client node...");

        this.requester.route("/api/v1/client/push/info").data(clientNode).retrieveMono(Void.class).subscribe();
    }

}
