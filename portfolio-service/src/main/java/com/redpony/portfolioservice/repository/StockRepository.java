package com.redpony.portfolioservice.repository;

import com.redpony.portfolioservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
        Stock findByUserName(String userName);
        Stock findByUserNameAndSymbol(String userName, String symbol);
}
