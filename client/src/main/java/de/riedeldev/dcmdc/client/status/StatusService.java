package de.riedeldev.dcmdc.client.status;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import de.riedeldev.dcmdc.client.ClientApplication;
import de.riedeldev.dcmdc.core.model.ClientStatus;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Service
public class StatusService {

    private DirectProcessor<ClientStatus> statusProcessor = DirectProcessor.create();
    private FluxSink<ClientStatus> statusSink = statusProcessor.sink();

    @PostConstruct
    protected void postConstruct() {
        Flux.interval(Duration.ofMillis(1000)).subscribe(t -> {
            var s = new ClientStatus();
            s.setUuid(ClientApplication.clientUuid);
            s.setMemoryUsage((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1_000_000.0f);
            s.setMaxMemory(Runtime.getRuntime().maxMemory() / 1_000_000.0f);
            s.setCpuUsage((float) (Math.random() * 100.0));
            s.setTimestamp(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli());
            this.statusSink.next(s);
        });
    }

    public Flux<ClientStatus> clientStatus() {
        return this.statusProcessor;
    }

}
