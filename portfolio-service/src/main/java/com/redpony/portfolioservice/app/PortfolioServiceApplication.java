package com.redpony.portfolioservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.redpony.portfolioservice.repository")
@EntityScan(basePackages = "com.redpony.portfolioservice.model")
@ComponentScan(basePackages = "com.redpony.portfolioservice.service")
@SpringBootApplication
@EnableJpaAuditing
public class PortfolioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioServiceApplication.class, args);
	}

}
