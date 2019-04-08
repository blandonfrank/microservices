package com.redpony.transactionsservice.controller;

import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.model.TransactionType;
import com.redpony.transactionsservice.service.TransactionService;
import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    RestTemplate restTemplate;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    //Once authentication is added, only allow admins to call this
    @GetMapping("/transactions")
    public List<Transaction> getAllTransaction(){
        return transactionService.getAllTransactions();
    }

    @RequestMapping("/transaction/{id}")
    public Transaction getTransaction(@PathVariable("id") Long id){
        log.info("Request to get transaction: {}", id);
        return verifyTransactionExists(id);
    }

    @RequestMapping("/transactions/user/{username}")
    public List<Transaction> getTransactionsByUser(@PathVariable("username") String username){
        log.info("Request to get transactions for user: {}", username);
        return transactionService.getByUserName(username);
    }

    @PostMapping("/transaction")
    public  ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction){
        log.info("Request to create transaction: {}");
        return ResponseEntity.ok().body(transactionService.createTransaction(transaction));
    }

    @PutMapping("/transaction")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction){
        log.info("Request to update transaction: {}", transaction);

        //check if transaction exist, else throw and exception
        verifyTransactionExists(transaction.getId());
        return ResponseEntity.ok().body(transactionService.updateTransaction(transaction));
    }

    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") Long id){
        log.info("Request to delete transaction: {}", id);

        //check if transaction exist, else throw and exception
        verifyTransactionExists(id);
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    //helper method to verify that the transaction exists before trying to do anything
    private Transaction verifyTransactionExists(Long id){
       return transactionService.getById(id).orElseThrow(() ->
                new NoSuchElementException("The transaction does not exists " + id));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String notFoundHandler(NoSuchElementException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidArgumentException.class)
    public String invalidRequestHanlder(InvalidArgumentException ex){
        return ex.getMessage();
    }
}
