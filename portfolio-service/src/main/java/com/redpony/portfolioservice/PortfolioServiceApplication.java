package com.redpony.portfolioservice;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import com.redpony.portfolioservice.listener.TransactionMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.redpony.portfolioservice.repository")
@EntityScan(basePackages = "com.redpony.portfolioservice.model")
@ComponentScan({"com.redpony.portfolioservice.*"})
@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
public class PortfolioServiceApplication {

	public final static String FINANCIAL_MESSAGE_QUEUE = "financial-message-queue";

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(FINANCIAL_MESSAGE_QUEUE);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(TransactionMessageListener receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(PortfolioServiceApplication.class, args);
	}

}
