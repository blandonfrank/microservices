package com.redpony.transactionsservice.service;

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
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    StockRepository stockRepository;

    private RabbitTemplate rabbitTemplate;

    public Transaction getById(Long id){
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getByUserName(String userName){
        return transactionRepository.findByUserName(userName);
    }

    public Transaction createTransaction(String userName, String symbol, BigDecimal price, int shares, String type){
        Transaction transaction = new Transaction();
        transaction.setUserName(userName);
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

        return transaction;
    }

}
