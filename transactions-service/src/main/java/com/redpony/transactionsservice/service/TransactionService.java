package com.redpony.transactionsservice.service;

import com.redpony.transactionsservice.TransactionsServiceApplication;
import com.redpony.transactionsservice.events.TransactionCreatedEvent;
import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.repository.StockRepository;
import com.redpony.transactionsservice.repository.TransactionRepository;
import io.eventuate.tram.events.ResultWithEvents;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Collections.singletonList;

@Service
@Slf4j
public class TransactionService {

    private TransactionRepository transactionRepository;
    private StockRepository stockRepository;
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, StockRepository stockRepository, RabbitTemplate rabbitTemplate){
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }
    public Optional<Transaction> getById(Long id){
        return transactionRepository.findById(id);
    }

    public List<Transaction> getByUserName(String username){
        List<Transaction> transactions = Collections.emptyList();
        if(transactionRepository.existsByUsername(username))
            transactions = transactionRepository.findByUsername(username);
        return transactions;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction){
        log.info("Creating transaction: {}", transaction.toString());
        //send out a transaction saga event to kafka
        TransactionCreatedEvent transactionCreatedEvent = new TransactionCreatedEvent(transaction);
        ResultWithEvents<Transaction> transWithEvents = new ResultWithEvents<>(transaction, singletonList(transactionCreatedEvent));

        Transaction updatedTransaction = transWithEvents.result;
        transactionRepository.save(updatedTransaction);
        domainEventPublisher.publish(Transaction.class, updatedTransaction.getId(), transWithEvents.events);
       // sendTransactionMessage(transaction); probably going to get rid of this

        return transaction;
    }

    public void sendTransactionMessage(Transaction transaction){
        Map<String, String> transactionMap = new HashMap<>();
        transactionMap.put("username", transaction.getUsername());
        transactionMap.put("symbol", transaction.getStock().getSymbol());
        transactionMap.put("amount", transaction.getTotal().getAmount().toPlainString());
        transactionMap.put("shares", String.valueOf(transaction.getStock().getShares()));
        transactionMap.put("type", transaction.getTransactionType().name());

        log.info("Sending transaction " + transaction.toString() + " message to queue ");
        rabbitTemplate.convertAndSend(TransactionsServiceApplication.FINANCIAL_MESSAGE_QUEUE, transactionMap);
    }

    @Transactional
    public Transaction updateTransaction(Transaction transaction){
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return updatedTransaction;
    }

    @Transactional
    public void deleteTransaction(Long id){
         transactionRepository.deleteById(id);
    }


    /*
    If the response from the portfolio service is a sucess/approval then the transaction
    status needs to be updated to approved
     */
    public void approveTransaction(Long id) {
        log.info("Transaction {} approved!", id);
    }

    /*
    If the response back from the portfolio service is a failure, then the transaction is rejected
    the status of the transaction needs to set to rejected, and a compensating transaction
    needs to take place
     */
    public void rejectTransaction(Long id) {
        log.info("Transaction {} rejected!", id);
    }
}
