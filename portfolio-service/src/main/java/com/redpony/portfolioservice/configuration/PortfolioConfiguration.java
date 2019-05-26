package com.redpony.portfolioservice.configuration;

import com.redpony.portfolioservice.service.PortfolioCommandHandler;
import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaParticipantConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(SagaParticipantConfiguration.class)
@EnableJpaRepositories (basePackages ={"io.eventuate.tram.sagas.orchestration", "com.redpony.portfolioservie.repository"})
@EnableAutoConfiguration
public class PortfolioConfiguration {

    @Bean
    public PortfolioCommandHandler portfolioCommandHandler() {
        return new PortfolioCommandHandler();
    }

    @Bean
    public CommandDispatcher consumerCommandDispatcher(PortfolioCommandHandler target,
                                                       SagaLockManager sagaLockManager) {
        return new SagaCommandDispatcher("portfolioCommandDispatcher", target.commandHandlerDefinitions());
    }
}
