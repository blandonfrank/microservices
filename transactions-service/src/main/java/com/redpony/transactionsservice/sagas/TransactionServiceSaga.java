package com.redpony.transactionsservice.sagas;

import com.redponny.common.replies.ApproveOrderCommand;
import com.redponny.common.replies.RejectOrderCommand;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Slf4j
public abstract class TransactionServiceSaga implements SimpleSaga<ProcessOrderSagaData> {

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
