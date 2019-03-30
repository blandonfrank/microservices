package com.redpony.transactionsservice.repository;

import com.redpony.transactionsservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
