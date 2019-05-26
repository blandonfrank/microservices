package com.redpony.portfolioservice.service;

import com.redponny.common.replies.ApproveOrderCommand;
import com.redponny.common.replies.RejectOrderCommand;
import com.redpony.common.commands.ReserveCreditCommand;
import com.redpony.portfolioservice.exceptions.InsufficientFundsException;
import com.redpony.portfolioservice.model.Portfolio;
import com.redpony.portfolioservice.repository.PortfolioRepository;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;
@Slf4j
public class PortfolioCommandHandler {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("portfolioService")
                .onMessage(ReserveCreditCommand.class, this::reserveCredit)
                .build();
    }

    public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {
    log.info("Command received to reserve credit: " + cm.getMessage().getPayload());
        ReserveCreditCommand cmd = cm.getCommand();
        String userName = cmd.getUserName();
        Portfolio portfolio = portfolioRepository.findByUsername(userName);
        if(!Objects.isNull(portfolio)) {
            try {
                portfolio.reserveCredit(cmd.getTransId(), cmd.getAmount());
                portfolio.addStock(cmd.getSymbol(), cmd.getShares(), cmd.getAmount());
                return withSuccess(new ApproveOrderCommand());
            } catch (InsufficientFundsException e) {
                return withFailure(new RejectOrderCommand(cmd.getTransId()));
            }
        }else
            return withFailure(new RejectOrderCommand(cmd.getTransId()));
    }
}
