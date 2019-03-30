package com.redpony.transactionsservice.repository;

import com.redpony.transactionsservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserName(String username);
}
