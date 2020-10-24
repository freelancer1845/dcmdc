package de.riedeldev.dcmdc.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import de.riedeldev.dcmdc.server.registration.RegistrationService;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ServerApplication implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private RegistrationService registrationService;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	// @Bean
	// public SecurityWebFilterChain filterChain(ServerHttpSecurity security) {
	// 	return security.cors().and().authorizeExchange().anyExchange().permitAll().and().build();
	// }

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("### Docker Command Center Server Ready. ###");
	}

}
