package com.redpony.transactionsservice.repository;

import com.redpony.transactionsservice.model.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {
}
