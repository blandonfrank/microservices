package com.redpony.transactionsservice.sagas;

import com.redponny.common.replies.ApproveOrderCommand;
import com.redponny.common.replies.RejectOrderCommand;
import com.redpony.common.commands.ReserveCreditCommand;
import com.redpony.transactionsservice.model.Stock;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Slf4j
public class ProcessTransactionSaga implements SimpleSaga<ProcessTransactionSagaData> {

    private SagaDefinition<ProcessTransactionSagaData> sagaDefinition =
            step()
                    .withCompensation(this::reject)
                    .step()
                    .invokeParticipant(this::reserveCredit)
                    .step()
                    .invokeParticipant(this::approve)
                    .build();


    @Override
    public SagaDefinition<ProcessTransactionSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }


    /**
     * This is to be used when trying to buy stocks. We start a saga and send a command
     * to the portfolio service to check whether the user has enough funds or not to perform the transaction
     * @param data
     * @return
     */
    private CommandWithDestination reserveCredit(ProcessTransactionSagaData data) {
        log.info("Trying to reserve credit with portfolio services");
        long transactionId = data.getTransaction().getTransId();
        String userName = data.getTransaction().getUsername();
        BigDecimal amount = data.getTransaction().getAmount();
        Stock stock = data.getTransaction().getStock();
        String symbol = stock.getSymbol();
        Long shares = stock.getShares();
        BigDecimal price = stock.getPrice();

        return send(new ReserveCreditCommand(transactionId, userName, amount, symbol, shares, price))
                .to("portfolioService")
                .build();
    }

    public CommandWithDestination reject(ProcessTransactionSagaData data) {
        log.info("Command received to reject transaction");
        return send(new RejectOrderCommand(data.getTransaction().getTransId()))
                .to("transactionService")
                .build();
    }

    private CommandWithDestination approve(ProcessTransactionSagaData data) {
        log.info("Command received to approve transaction");
        return send(new ApproveOrderCommand(data.getTransaction().getTransId()))
                .to("transactionService")
                .build();
    }


}