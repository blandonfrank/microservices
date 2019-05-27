package com.redpony.transactionsservice.configuration;

import com.redpony.transactionsservice.sagas.TradeOrderSaga;
import com.redpony.transactionsservice.sagas.ProcessOrderSagaData;
import com.redpony.transactionsservice.service.OrderCommandHandler;
import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.orchestration.Saga;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages ={"io.eventuate.tram.sagas.orchestration", "com.redpony.transactionsservice.repository"})
@EnableAutoConfiguration
public class TransactionConfiguration {


    @Bean
    public SagaManager<ProcessOrderSagaData> createOrderSagaManager(Saga<ProcessOrderSagaData> saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public TradeOrderSaga tradeOrderSaga() {
        return new TradeOrderSaga();
    }

    @Bean
    public OrderCommandHandler orderCommandHandler() {
        return new OrderCommandHandler();
    }

    @Bean
    public CommandDispatcher orderCommandDispatcher(OrderCommandHandler target) {
        return new SagaCommandDispatcher("transactionCommandDispatcher", target.commandHandlerDefinitions());
    }
}
