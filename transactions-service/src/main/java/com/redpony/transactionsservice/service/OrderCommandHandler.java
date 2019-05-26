package com.redpony.transactionsservice.service;

import com.redponny.common.replies.ApproveOrderCommand;
import com.redponny.common.replies.RejectOrderCommand;
import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.repository.TransactionRepository;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

public class OrderCommandHandler {

    @Autowired
    private TransactionRepository transactionRepository;

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("transactionService")
                .onMessage(ApproveOrderCommand.class, this::approve)
                .onMessage(RejectOrderCommand.class, this::reject)
                .build();
    }

    public Message approve(CommandMessage<ApproveOrderCommand> cm) {
        long transactionId = cm.getCommand().getTransId();
        Transaction transaction = transactionRepository.getOne(transactionId);
        if(Objects.nonNull(transaction)) {
            transaction.approved();
            return withSuccess();
        }
        else
            return withFailure();
    }

    public Message reject(CommandMessage<RejectOrderCommand> cm) {
        long transactionId = cm.getCommand().getTransId();
        Transaction transaction = transactionRepository.getOne(transactionId);
        if (Objects.nonNull(transaction)) {
            transaction.rejected();
            return withSuccess();
        }else
            return withFailure();
    }
}
