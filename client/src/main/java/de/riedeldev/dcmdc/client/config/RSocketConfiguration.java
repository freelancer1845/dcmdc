package de.riedeldev.dcmdc.client.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RSocketConfiguration implements BeanPostProcessor {

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder rsocketRequesterBuilder,
            RSocketStrategies rSocketStrategies) {
        var targetBeans = listableBeanFactory.getBeansWithAnnotation(Controller.class);
        var socketAcceptor = RSocketMessageHandler.responder(rSocketStrategies, targetBeans.values().toArray());
        return rsocketRequesterBuilder.rsocketStrategies(rSocketStrategies)

                .rsocketConnector(connector -> connector.acceptor(socketAcceptor)).connectTcp("127.0.0.1", 18080)
                .doOnSuccess(s -> {
                    log.info("Successfully opened rsocket connection");
                }).block();
    }

}
