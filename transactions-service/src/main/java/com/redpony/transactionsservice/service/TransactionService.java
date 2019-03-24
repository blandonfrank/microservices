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

    public Transaction getById(Long id){
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getByUserName(String userName){
        return transactionRepository.findByUserName(userName);
    }

    public Transaction createTransaction(String username, String symbol, BigDecimal price, int shares, String type){
        log.info("Creating transaction: " + username +"\n" + symbol + "\n " + price + "\n" + shares + "\n" + type);

        Transaction transaction = new Transaction();
        transaction.setUserName(username);
        transaction.setTransactionType(TransactionType.valueOf(type));

        Stock stock = new Stock();
        stock.setPrice(price);
        stock.setShares(shares);
        stock.setSymbol(symbol);
        stockRepository.save(stock);

        //add stock to transaction
        transaction.setAmount(price.multiply(new BigDecimal(shares))); //set total amount of the transaction
        transaction.setStock(stock);

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

}
