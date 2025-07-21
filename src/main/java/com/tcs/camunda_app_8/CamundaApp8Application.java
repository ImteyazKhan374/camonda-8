package com.tcs.camunda_app_8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.camunda.zeebe.client.ZeebeClient;

@SpringBootApplication
public class CamundaApp8Application {

	public static void main(String[] args) {
		SpringApplication.run(CamundaApp8Application.class, args);
	}

	@Bean
	ZeebeClient zeebeClient() {
		return ZeebeClient.newClientBuilder().gatewayAddress("127.0.0.1:26500") // Must match application.yml
				.usePlaintext() // Critical - must match Zeebe broker config
				.build();
	}
}
