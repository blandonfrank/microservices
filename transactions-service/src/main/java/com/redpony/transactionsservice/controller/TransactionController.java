package com.redpony.transactionsservice.controller;

import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @RequestMapping("/id/{id})")
    public Transaction getTransaction(@PathVariable("id") Long id){
        return transactionService.getById(id);
    }

    @RequestMapping("/user/{username}")
    public List<Transaction> getTransactions(@PathVariable("username") String username){
        return transactionService.getByUserName(username);
    }

    @PostMapping("/create")
    public Transaction createTransaction(@RequestParam("username") String username,
                                         @RequestParam("symbol") String symbol,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("shares") int shares,
                                         @RequestParam("type") String type){
       return transactionService.createTransaction(username, symbol, price, shares, type);
    }
}
