package com.redpony.transactionsservice.controller;

import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    //Once authentication is added, only allow admins to call this
    @GetMapping("/admin/all")
    public List<Transaction> getAllTransaction(){
        return transactionService.getAllTransactions();
    }

    @RequestMapping("/id/{id}")
    public Transaction getTransaction(@PathVariable("id") Long id){
        log.info("Request to get transaction: {}", id);
        return transactionService.getById(id);
    }

    @RequestMapping("/user/{username}")
    public List<Transaction> getTransactions(@PathVariable("username") String username){
        log.info("Request to get transactions for user: {}", username);
        return transactionService.getByUserName(username);
    }

    @PostMapping("/create")
    public Transaction createTransaction(@RequestParam("username") String username,
                                         @RequestParam("symbol") String symbol,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("shares") int shares,
                                         @RequestParam("type") String type){
        log.info("Request to create transaction: {}");
       return transactionService.createTransaction(username, symbol, price, shares, type);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction){
        log.info("Request to update transaction: {}", transaction);
        return ResponseEntity.ok().body(transactionService.updateTransaction(transaction));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") Long id){
        log.info("Request to delete transaction: {}", id);
            if(!transactionService.deleteTransaction(id))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok().build();
    }
}
