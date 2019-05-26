package com.redpony.portfolioservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.redpony.portfolioservice.exceptions.InsufficientFundsException;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "portfolios")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor
public class Portfolio extends AbstractEntity {
    @NotBlank
    @NonNull
    private String username;
    @NonNull
    private String owner;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal cash = BigDecimal.ZERO;
    private String sentiment;
    private BigDecimal performance = BigDecimal.ZERO;
    private double risk;

    @ElementCollection
    private Map<Long, BigDecimal> creditReservations = Collections.emptyMap();

    @ElementCollection
    private Map<String, Stock> stocks = Collections.emptyMap();;

    private BigDecimal getAvailableBalance(){
        return cash;
    }

    public void reserveCredit(Long transId, BigDecimal transactionTotal) {
        if (getAvailableBalance().compareTo(transactionTotal) >= 0) {
            creditReservations.put(transId, transactionTotal);
            cash = cash.subtract(transactionTotal);
        } else
            throw new InsufficientFundsException("You ain't got no money!");
    }

    public void addStock(String symbol, Long newShares, BigDecimal newCost) {
        if (!Objects.isNull(stocks)) {
            if (stocks.containsKey(symbol)) {
                Stock updatedStock = stocks.get(symbol);
                Long currentShares = updatedStock.getShares();
                BigDecimal currentCost = updatedStock.getTotalCost();
                BigDecimal newTotalCost = currentCost.add(newCost);

                BigDecimal newAvgCost = newTotalCost.divide(new BigDecimal(currentShares + newShares));
                updatedStock.setShares(currentShares + newShares);
                updatedStock.setAverageCost(newAvgCost);
                updatedStock.setTotalCost(newTotalCost);

                stocks.put(symbol, updatedStock);
            } else {
                Stock newStock = new Stock(symbol);
                newStock.setShares(newShares);
                newStock.setTotalCost(newCost);
                BigDecimal avgCost = newCost.divide(new BigDecimal(newShares));
                newStock.setAverageCost(avgCost);
                stocks.put(symbol, newStock);
            }
        }
    }

}
