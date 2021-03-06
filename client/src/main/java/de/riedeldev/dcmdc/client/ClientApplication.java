package de.riedeldev.dcmdc.client;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import de.riedeldev.dcmdc.client.dockeraccess.services.ContainerFactoryService;
import de.riedeldev.dcmdc.client.dockeraccess.services.ListContainerServices;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ClientApplication implements ApplicationListener<ApplicationReadyEvent> {

	public static String clientUuid;

	@Autowired
	private ListContainerServices listContainersService;

	@Autowired
	private ContainerFactoryService factory;

	public static void main(String[] args) {

		SpringApplication.run(ClientApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

	}

}
