package com.redpony.discoveryeurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DiscoveryEurekaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryEurekaServiceApplication.class, args);
	}

}
