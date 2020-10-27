package de.riedeldev.dcmdc.client.config;

import java.nio.charset.Charset;
import java.time.Duration;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetrySpec;

@Configuration
@Slf4j
public class RSocketConfiguration implements BeanPostProcessor {

    @Value("${api.id:example_api_id_string}")
    private String apiId;

    @Value("${api.secret:very-secret-api-secret}")
    private String apiSecret;

    @Value("${api.host:127.0.0.1}")
    private String apiHost;

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Bean
    Mono<RSocketRequester> rSocketRequester(RSocketRequester.Builder rsocketRequesterBuilder,
            RSocketStrategies rSocketStrategies) {
        var targetBeans = listableBeanFactory.getBeansWithAnnotation(Controller.class);
        var socketAcceptor = RSocketMessageHandler.responder(rSocketStrategies, targetBeans.values().toArray());
        log.info("Registering As Client at {} with API_ID: {}", this.apiHost, this.apiId);
        return rsocketRequesterBuilder.rsocketStrategies(rSocketStrategies)

                .rsocketConnector(connector -> connector.acceptor(socketAcceptor)
                        .reconnect(RetrySpec.backoff(Long.MAX_VALUE, Duration.ofSeconds(10))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .setupData(new SetupInfo(this.apiId, this.apiSecret.getBytes(Charset.forName("UTF-8"))))
                .connectTcp(this.apiHost, 18080).doOnSuccess(s -> {
                    log.info("Successfully opened rsocket connection");
                }); 
    }

    @Data
    @AllArgsConstructor
    public static final class SetupInfo {
        String apiId;
        byte[] apiSecret;
    }

}
