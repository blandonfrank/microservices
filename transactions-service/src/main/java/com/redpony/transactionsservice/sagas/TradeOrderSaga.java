package com.redpony.transactionsservice.sagas;

import com.redpony.common.commands.ReserveCreditCommand;
import com.redpony.common.commands.ReserveDebitCommand;
import com.redpony.common.commands.ReserveDepositCommand;
import com.redpony.common.commands.ReserveWithdrawCommand;
import com.redpony.transactionsservice.model.Stock;
import com.redpony.transactionsservice.model.Transaction;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Slf4j
public class TradeOrderSaga extends TransactionServiceSaga implements SimpleSaga<ProcessOrderSagaData> {

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
     * This is to be used when trying to buy stocks. We start a saga and send a command
     * to the portfolio service to check whether the user has enough funds or not to perform the transaction
     * @param data
     * @return
     */
    private CommandWithDestination sendCommandByTransactionType(ProcessOrderSagaData data) {
        log.info("Trying to reserve credit with portfolio services");
        long transactionId = data.getTransaction().getTransId();
        String userName = data.getTransaction().getUsername();
        BigDecimal amount = data.getTransaction().getAmount();


        switch (data.getTransaction().getTransactionType()) {
            case BUY:
                Stock stock = getStockInfo(data.getTransaction());

                return send(new ReserveCreditCommand(transactionId, userName, amount, stock.getSymbol(), stock.getShares(), stock.getPrice()))
                        .to(PORTFOLIO_DESTINATION)
                        .build();
            case SELL:
                stock = getStockInfo(data.getTransaction());

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

    private Stock getStockInfo(Transaction transaction) {
        Stock stock = transaction.getStock();
        String symbol = stock.getSymbol();
        Long shares = stock.getShares();
        BigDecimal price = stock.getPrice();
        return stock;
    }


}