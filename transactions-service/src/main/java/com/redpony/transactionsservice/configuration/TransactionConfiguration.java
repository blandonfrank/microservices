package com.redpony.transactionsservice.configuration;

import com.redpony.transactionsservice.service.PortfolioEventConsumer;
import com.redpony.transactionsservice.service.TransactionService;
import io.eventuate.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({TramEventsPublisherConfiguration.class,
        TramMessageProducerJdbcConfiguration.class,
        TramJdbcKafkaConfiguration.class
        })
public class TransactionConfiguration {

    @Bean
    public PortfolioEventConsumer portfolioEventConsumer() {
        return new PortfolioEventConsumer();
    }
    @Bean
    public DomainEventDispatcher domainEventDispatcher(PortfolioEventConsumer portfolioEventConsumer, MessageConsumer messageConsumer) {
        return new DomainEventDispatcher("PortfolioServiceEvents", portfolioEventConsumer.domainEventHandlers(), messageConsumer);
    }
}
