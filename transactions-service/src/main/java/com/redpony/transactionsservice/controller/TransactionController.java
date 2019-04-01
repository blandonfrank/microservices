package com.redpony.transactionsservice.controller;

import com.redpony.transactionsservice.model.Transaction;
import com.redpony.transactionsservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

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
        return verifyTransactionExists(id);
    }

    @RequestMapping("/user/{username}")
    public List<Transaction> getTransactions(@PathVariable("username") String username){
        log.info("Request to get transactions for user: {}", username);
        return transactionService.getByUserName(username);
    }

    @PostMapping("/create")
    public  ResponseEntity<Transaction>  createTransaction(@Valid @RequestBody Transaction transaction){
        log.info("Request to create transaction: {}");
        return ResponseEntity.ok().body(transactionService.createTransaction(transaction));
    }

    @PutMapping("/update")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction){
        log.info("Request to update transaction: {}", transaction);

        //check if transaction exist, else throw and exception
        verifyTransactionExists(transaction.getId());
        return ResponseEntity.ok().body(transactionService.updateTransaction(transaction));
    }

    @DeleteMapping("/delete/{id}")
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
}
