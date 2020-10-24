package de.riedeldev.dcmdc.server.clients;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.core.model.ClientStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;

@Service
public class ClientService {

    private Map<String, ClientHandle> handles = new HashMap<>();

    public synchronized void handleNewRSocketClient(String uuid, RSocketRequester requester) {
        if (this.handles.containsKey(uuid)) {
            this.updateHandle(uuid, requester);
        } else {
            this.createHandle(uuid, requester);
        }
    }

    public Flux<ClientStatus> clientStatus(String uuid) {
        return Flux.defer(() -> {
            if (this.handles.containsKey(uuid) == false) {
                throw new IllegalArgumentException("No client with uuid: " + uuid + " available.");
            }
            return this.handles.get(uuid).status;
        });
    }

    private static final class ClientHandle {
        ReplayProcessor<RSocketRequester> rsocket = ReplayProcessor.cacheLast();
        FluxSink<RSocketRequester> rsocketSink = rsocket.sink();

        Flux<ClientStatus> status;
    }

    private void updateHandle(String uuid, RSocketRequester requester) {
        this.handles.get(uuid).rsocketSink.next(requester);
    }

    private void createHandle(String uuid, RSocketRequester requester) {
        var handle = new ClientHandle();
        handle.rsocketSink.next(requester);
        this.handles.put(uuid, handle);

        handle.status = handle.rsocket.switchMap(req -> {
            return req.route("/api/v1/client/status").retrieveFlux(ClientStatus.class);
        });
    }
}
