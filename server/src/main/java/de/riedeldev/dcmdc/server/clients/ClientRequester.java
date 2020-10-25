package de.riedeldev.dcmdc.server.clients;

import org.springframework.messaging.rsocket.RSocketRequester;

import de.riedeldev.dcmdc.core.model.requests.CreateContainerAnswer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ClientRequester {

    private final Flux<RSocketRequester> _requester;

    public ClientRequester(Flux<RSocketRequester> requester) {
        this._requester = requester;
    }


    // public Mono<CreateContainerAnswer


}
