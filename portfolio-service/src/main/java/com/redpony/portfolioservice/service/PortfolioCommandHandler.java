package com.redpony.portfolioservice.service;

import com.redponny.common.replies.ApproveOrderCommand;
import com.redponny.common.replies.RejectOrderCommand;
import com.redpony.common.commands.ReserveCreditCommand;
import com.redpony.common.commands.ReserveDebitCommand;
import com.redpony.common.commands.ReserveDepositCommand;
import com.redpony.common.commands.ReserveWithdrawCommand;
import com.redpony.portfolioservice.exceptions.InsufficientFundsException;
import com.redpony.portfolioservice.exceptions.InsufficientHoldingException;
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
                .onMessage(ReserveDebitCommand.class, this::reserveDebit)
                .onMessage(ReserveDepositCommand.class, this::deposit)
                .onMessage(ReserveWithdrawCommand.class, this::withdraw)
                .build();
    }

    /**
     * If a portfolio exists for the username sent through the ReserveCreditCommand from other services
     * This is used for BUYING stocks
     * a call is made to the portfolio to add the given stock bought into the portfolio's current stocks holding
     * If an account does not have enough funds to buy the given stock then an InsufficientFundsException is thrown
     * @param cm
     * @return
     * @throws InsufficientFundsException
     */
    public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {
    log.info("Command received to reserve credit - buying: " + cm.getMessage().getPayload());
        ReserveCreditCommand cmd = cm.getCommand();
        String userName = cmd.getUserName();
        Portfolio portfolio = portfolioRepository.findByUsername(userName);
        if(!Objects.isNull(portfolio)) {
            try {
                portfolio.reserveCredit(cmd.getTransId(), cmd.getAmount());
                portfolio.addStock(cmd.getSymbol(), cmd.getShares(), cmd.getAmount());
                return withSuccess(new ApproveOrderCommand(cmd.getTransId()));
            } catch (InsufficientFundsException e) {
                return withFailure(new RejectOrderCommand(cmd.getTransId()));
            }
        }else
            return withFailure(new RejectOrderCommand(cmd.getTransId()));
    }

    public Message reserveDebit(CommandMessage<ReserveDebitCommand> cm) {
        log.info("Command received to reserve debit - selling: " + cm.getMessage().getPayload());
        ReserveDebitCommand cmd = cm.getCommand();
        String userName = cmd.getUserName();
        Portfolio portfolio = portfolioRepository.findByUsername(userName);
        if(!Objects.isNull(portfolio) && !Objects.isNull(portfolio.getStocks()) && portfolio.getStocks().containsKey(cmd.getSymbol())) {
            try {
                portfolio.updateStockHolding(cmd.getTransId(), cmd.getSymbol(), cmd.getShares(), cmd.getAmount());
                return withSuccess(new ApproveOrderCommand(cmd.getTransId()));
            } catch (InsufficientHoldingException e) {
                return withFailure(new RejectOrderCommand(cmd.getTransId()));
            }
        }else
            return withFailure(new RejectOrderCommand(cmd.getTransId()));
    }

    /**
     * If a portfolio exists for the username sent through the ReserveDepositCommand from other services
     * a call is made to the portfolio to add the amount deposited as part of the portfolio's cash holding
     * @param cm
     * @return
     */
    public Message deposit(CommandMessage<ReserveDepositCommand> cm) {
        log.info("Command received to deposit money: " + cm.getMessage().getPayload());
        ReserveDepositCommand cmd = cm.getCommand();
        String userName = cmd.getUserName();
        Portfolio portfolio = portfolioRepository.findByUsername(userName);
        if (!Objects.isNull(portfolio)) {
            portfolio.deposit(cmd.getTransId(), cmd.getAmount());
            return withSuccess(new ApproveOrderCommand(cmd.getTransId()));
        } else
            return withFailure(new RejectOrderCommand(cmd.getTransId()));
    }

    /**
     * If a portfolio exists for the username sent through the ReserveWithdrawCommand from other services
     * a call is made to the portfolio to debit the amount withdrawn from the portfolio's cash holding
     *
     * If the portfolio does not hold enough funds an InsufficientFundsException is thrown
     * @param cm
     * @return
     * @throws InsufficientFundsException
     */
    public Message withdraw(CommandMessage<ReserveWithdrawCommand> cm) {
        log.info("Command received to withdraw money: "+ cm.getMessage().getPayload());
        ReserveWithdrawCommand cmd = cm.getCommand();
        String userName = cmd.getUserName();
        Portfolio portfolio = portfolioRepository.findByUsername(userName);
        if(!Objects.isNull(portfolio)) {
            try {
                portfolio.withdraw(cmd.getTransId(), cmd.getAmount());
                return withSuccess(new ApproveOrderCommand());
            } catch (InsufficientFundsException e) {
                return withFailure(new RejectOrderCommand(cmd.getTransId()));
            }
        }else
            return withFailure(new RejectOrderCommand(cmd.getTransId()));
    }
}
