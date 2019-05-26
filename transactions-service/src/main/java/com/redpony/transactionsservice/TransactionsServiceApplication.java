package com.redpony.transactionsservice;

import com.redpony.transactionsservice.configuration.TransactionConfiguration;
import io.eventuate.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.commands.common.ChannelMapping;
import io.eventuate.tram.commands.common.DefaultChannelMapping;
import io.eventuate.tram.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.sagas.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.sagas.participant.SagaParticipantConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EntityScan(basePackages = "com.redpony.transactionsservice.model")
@ComponentScan
@EnableEurekaClient
@EnableRabbit
@Configuration
@Import({TransactionConfiguration.class,
		TramEventsPublisherConfiguration.class,
		TramCommandProducerConfiguration.class,
		SagaOrchestratorConfiguration.class,
		TramJdbcKafkaConfiguration.class,
		SagaParticipantConfiguration.class})

public class TransactionsServiceApplication {

//	public final static String FINANCIAL_MESSAGE_QUEUE = "financial-message-queue";
//
//	@Bean
//	Queue queue() {
//		return new Queue(FINANCIAL_MESSAGE_QUEUE, false);
//	}
//
//	@Bean
//	TopicExchange exchange() {
//		return new TopicExchange("financial-messages-exchange");
//	}
//
//
//	@Bean
//	Binding binding(Queue queue, TopicExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(FINANCIAL_MESSAGE_QUEUE);
//	}

	@Bean
	public ChannelMapping channelMapping() {
		return DefaultChannelMapping.builder().build();
	}

	public static void main(String[] args) {
		SpringApplication.run(TransactionsServiceApplication.class, args);
	}

}
