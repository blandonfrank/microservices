package com.redpony.transactionsservice.sagas;

import com.redponny.common.replies.ApproveOrderCommand;
import com.redponny.common.replies.RejectOrderCommand;
import com.redpony.common.commands.ReserveCreditCommand;
import com.redpony.common.commands.ReserveDebitCommand;
import com.redpony.common.commands.ReserveDepositCommand;
import com.redpony.common.commands.ReserveWithdrawCommand;
import com.redpony.transactionsservice.model.Stock;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Slf4j
public class TradeOrderSaga implements SimpleSaga<ProcessOrderSagaData> {

    private static final String PORTFOLIO_DESTINATION = "portfolioService";

    private SagaDefinition<ProcessOrderSagaData> sagaDefinition =
            step()
                    .withCompensation(this::reject)
                    .step()
                    .invokeParticipant(this::sendCommandByTransactionType)
                    .step()
                    .invokeParticipant(this::approve)
                    .build();


    @Override
    public SagaDefinition<ProcessOrderSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }


    /**
     * This is to send commands from the transaction service to the portfolio services. Depending on the type of the transaction
     * A different command is send out.
     * If BUY       - ReserveCreditCommand (transaction id, username, symbol, # of shares, price)
     * IF SELL      - ReserveDebitCommand  (transaction id, username, symbol, # of shares, price)
     * IF DEPOSIT   - ReserveDepositCommand (transaction id, username, amount)
     * IF WITHDRAW  - ReserveWithdrawCommand (transaction id, username, amount)
     * @param data
     * @return
     */
    private CommandWithDestination sendCommandByTransactionType(ProcessOrderSagaData data) {
        log.info("Trying to send command to " + data.getTransaction().getTransactionType().toString() + " to portfolio services");
        long transactionId = data.getTransaction().getTransId();
        String userName = data.getTransaction().getUsername();
        BigDecimal amount = data.getTransaction().getAmount();


        switch (data.getTransaction().getTransactionType()) {
            case BUY:
                Stock stock = data.getTransaction().getStock();

                return send(new ReserveCreditCommand(transactionId, userName, amount, stock.getSymbol(), stock.getShares(), stock.getPrice()))
                        .to(PORTFOLIO_DESTINATION)
                        .build();
            case SELL:
                stock = data.getTransaction().getStock();

                return send(new ReserveDebitCommand(transactionId, userName, amount, stock.getSymbol(), stock.getShares(), stock.getPrice()))
                        .to(PORTFOLIO_DESTINATION)
                        .build();
            case DEPOSIT:
                return send(new ReserveDepositCommand(transactionId, userName, amount))
                        .to(PORTFOLIO_DESTINATION)
                        .build();
            case WITHDRAW:
                return send(new ReserveWithdrawCommand(transactionId, userName, amount))
                        .to(PORTFOLIO_DESTINATION)
                        .build();
        }
        return null;
    }

    public CommandWithDestination reject(ProcessOrderSagaData data) {
        log.info("Command received to reject transaction");
        return send(new RejectOrderCommand(data.getTransaction().getTransId()))
                .to("transactionService")
                .build();
    }

    public CommandWithDestination approve(ProcessOrderSagaData data) {
        log.info("Command received to approve transaction");
        return send(new ApproveOrderCommand(data.getTransaction().getTransId()))
                .to("transactionService")
                .build();
    }


}