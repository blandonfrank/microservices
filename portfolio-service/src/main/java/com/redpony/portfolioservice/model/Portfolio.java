package com.redpony.portfolioservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redpony.portfolioservice.exceptions.InsufficientFundsException;
import com.redpony.portfolioservice.exceptions.InsufficientHoldingException;
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

    @JsonIgnore
    @ElementCollection
    private Map<Long, BigDecimal> creditReservations = Collections.emptyMap();

    @JsonIgnore
    @ElementCollection
    private Map<Long, BigDecimal> debitReservations = Collections.emptyMap();

    @ElementCollection
    private Map<String, Stock> stocks = Collections.emptyMap();;

    /**
     * Returns the current cash holding in the portfolio.
     * @return
     */
    private BigDecimal getAvailableBalance(){
        return cash;
    }

    /**
     * Try to reserve a credit on the portfolio. The word "Reserve" is used instead of actually doing a credit right away
     * because in case of a transaction failure, a compensating transaction will be performed to not remove funds incorrectly
     * from the portfolio.
     * A credit reservation can only happen if the account has available funds.
     * @param transId
     * @param transactionTotal
     * @throws InsufficientFundsException
     */
    public void reserveCredit(Long transId, BigDecimal transactionTotal) {
        if (getAvailableBalance().compareTo(transactionTotal) >= 0) {
            creditReservations.put(transId, transactionTotal);
            cash = cash.subtract(transactionTotal);
        } else
            throw new InsufficientFundsException("You ain't got no money!");
    }

    /**
     * Add stock to the portfolio. If the portfolio already holds the given stock - using the symbol for lookup
     * Then we increase the number of shares of the portfolio's current holding, also the average cost is recalculated and updated
     *
     * If the portfolio does not hold the stock, then it's inserted as a new holding
     * @param symbol
     * @param newShares
     * @param newCost
     *
     */
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

    /**
     * Deposit funds into the portfolio and make them available. Also, add a record into the debit reservation map
     * to keep track of debits done to the portfolio
     * @param transId
     * @param amount
     */
    public void deposit(Long transId, BigDecimal amount){
        debitReservations.put(transId, amount);
        cash = cash.add(amount);
    }

    /**
     * Withdraw from available funds - if the portfolio does not have enough funds an InsufficientFundsException is thrown
     * A record is inserted into the credit reservations map, to keep track of credits done to the portfolio
     * @param transId
     * @param amount
     * @throws InsufficientFundsException
     */
    public void withdraw(Long transId, BigDecimal amount){
        if (getAvailableBalance().compareTo(amount) >= 0) {
            creditReservations.put(transId, amount);
            cash = cash.subtract(amount);
        }else{
            throw new InsufficientFundsException("You ain't got no money!");
        }
    }

    /**
     * Update the current stock holding for a particular stock. If all shares of a stock are getting sold
     * remove that stock from the current stock holding.
     * @param symbol
     * @param sharesToSell
     * @param amount
     * @throws InsufficientHoldingException when trying to sell more shares than current holding
     */
    public void updateStockHolding(Long transId, String symbol, Long sharesToSell, BigDecimal amount) {
        Stock stock = stocks.get(symbol);
        if(stock.getShares() < sharesToSell)
            throw new InsufficientHoldingException("Not holding enough shares");

        if(stock.getShares() - sharesToSell == 0){ //All holding/shares for this stock are getting sold
            stocks.remove(symbol); //remove from current stock holdings
        }else{
            Long newSharesCount = stock.getShares() - sharesToSell;
            stock.setShares(newSharesCount); //update stock with new shares count
            stocks.put(symbol, stock);
        }
        debitReservations.put(transId, amount);
        cash = cash.add(amount); //add the amount to the current cash balance
    }
}
