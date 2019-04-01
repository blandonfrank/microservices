package com.redpony.transactionsservice.service;

import com.redpony.transactionsservice.TransactionsServiceApplication;
import com.redpony.transactionsservice.model.Stock;
import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.model.TransactionType;
import com.redpony.transactionsservice.repository.StockRepository;
import com.redpony.transactionsservice.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    private TransactionRepository transactionRepository;
    private StockRepository stockRepository;
    private RabbitTemplate rabbitTemplate;

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

    public List<Transaction> getByUserName(String userName){
        return transactionRepository.findByUserName(userName);
    }

    public Transaction createTransaction(Transaction transaction){
        log.info("Creating transaction: {}", transaction.toString());

        transactionRepository.save(transaction);
        sendTransactionMessage(transaction);

        return transaction;
    }

    public void sendTransactionMessage(Transaction transaction){
        Map<String, String> transactionMap = new HashMap<>();
        transactionMap.put("username", transaction.getUserName());
        transactionMap.put("symbol", transaction.getStock().getSymbol());
        transactionMap.put("amount", transaction.getAmount().toPlainString());
        transactionMap.put("shares", String.valueOf(transaction.getStock().getShares()));
        transactionMap.put("type", transaction.getTransactionType().name());

        log.info("Sending transaction " + transaction.toString() + " message to queue ");
        rabbitTemplate.convertAndSend(TransactionsServiceApplication.FINANCIAL_MESSAGE_QUEUE, transactionMap);
    }

    public Transaction updateTransaction(Transaction transaction){
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return updatedTransaction;
    }

    public void deleteTransaction(Long id){
         transactionRepository.deleteById(id);
    }

}
