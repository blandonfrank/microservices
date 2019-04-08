package com.redpony.portfolioservice.repository;

import com.redpony.portfolioservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
//        Stock findByUsername(String username);
//        Stock findByUsernameAndSymbol(String username, String symbol);
}
