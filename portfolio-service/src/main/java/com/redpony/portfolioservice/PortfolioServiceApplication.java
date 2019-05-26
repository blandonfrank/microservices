package com.redpony.portfolioservice;

import com.redpony.portfolioservice.configuration.PortfolioConfiguration;
import io.eventuate.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.commands.common.ChannelMapping;
import io.eventuate.tram.commands.common.DefaultChannelMapping;
import io.eventuate.tram.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.sagas.orchestration.SagaOrchestratorConfiguration;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.redpony.portfolioservice.repository")
@EntityScan(basePackages = "com.redpony.portfolioservice.model")
@ComponentScan
@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
@Configuration
@Import({PortfolioConfiguration.class,
		TramEventsPublisherConfiguration.class,
		TramCommandProducerConfiguration.class,
		SagaOrchestratorConfiguration.class,
		TramJdbcKafkaConfiguration.class})
public class PortfolioServiceApplication {

	public final static String FINANCIAL_MESSAGE_QUEUE = "financial-message-queue";

	@Bean
	public ChannelMapping channelMapping() {
		return DefaultChannelMapping.builder().build();
	}
//	@Bean
//	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//											 MessageListenerAdapter listenerAdapter) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		container.setQueueNames(FINANCIAL_MESSAGE_QUEUE);
//		container.setMessageListener(listenerAdapter);
//		return container;
//	}
//
//	@Bean
//	MessageListenerAdapter listenerAdapter(TransactionMessageListener receiver) {
//		return new MessageListenerAdapter(receiver, "receiveMessage");
//	}

	public static void main(String[] args) {
		SpringApplication.run(PortfolioServiceApplication.class, args);
	}

}
