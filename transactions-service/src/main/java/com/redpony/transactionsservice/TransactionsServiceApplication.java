package com.redpony.transactionsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableEurekaClient
public class TransactionsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionsServiceApplication.class, args);
	}

}
