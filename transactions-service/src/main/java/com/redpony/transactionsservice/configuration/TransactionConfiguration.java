package com.redpony.transactionsservice.configuration;

import com.redpony.transactionsservice.sagas.ProcessTransactionSaga;
import com.redpony.transactionsservice.sagas.ProcessTransactionSagaData;
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
    public SagaManager<ProcessTransactionSagaData> createOrderSagaManager(Saga<ProcessTransactionSagaData> saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public ProcessTransactionSaga createOrderSaga() {
        return new ProcessTransactionSaga();
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
