package de.riedeldev.dcmdc.client.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import de.riedeldev.dcmdc.core.model.ClientStatus;
import reactor.core.publisher.Flux;

@Controller
public class StatusController {

    @Autowired
    private StatusService service;

    @MessageMapping("/api/v1/client/status")
    public Flux<ClientStatus> clientStatusFlux() {
        return this.service.clientStatus();
    }

}
