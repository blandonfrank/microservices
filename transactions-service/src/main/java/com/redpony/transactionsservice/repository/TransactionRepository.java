package com.redpony.transactionsservice.repository;

import com.redpony.transactionsservice.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByUserName(String username);
}
