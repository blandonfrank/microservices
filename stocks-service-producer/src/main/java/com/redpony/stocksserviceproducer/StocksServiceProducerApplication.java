package com.redpony.stocksserviceproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StocksServiceProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocksServiceProducerApplication.class, args);
	}

}
