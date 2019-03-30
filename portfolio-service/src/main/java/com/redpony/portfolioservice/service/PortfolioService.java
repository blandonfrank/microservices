package com.redpony.portfolioservice.service;

import com.redpony.portfolioservice.model.Portfolio;
import com.redpony.portfolioservice.model.Stock;
import com.redpony.portfolioservice.repository.PortfolioRepository;
import com.redpony.portfolioservice.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@Slf4j
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<Portfolio> getAllPortfolios(){
        return portfolioRepository.findAll();
    }


    public Portfolio getPortfolio(@PathVariable("username") final String username){
        Portfolio portfolio = portfolioRepository.findByUsername(username);

        log.info("Looking up portfolio for " + username);
        if(Objects.isNull(portfolio))
            return new Portfolio();

        return portfolio;
    }

    public Portfolio createPorfolio(@PathVariable("username") final String username){
        Portfolio portfolio = new Portfolio();
        if(!StringUtils.isEmpty(username)){
            if(portfolioRepository.findByUsername(username) !=null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Portfolio already exists for: " + username );

            portfolio.setUsername(username);
            portfolioRepository.save(portfolio);
        }
        return portfolio;
    }

    public Portfolio updatePortfolio(@PathVariable("username") final String username, @PathParam("symbol") final String symbol, @PathParam("shares") int shares){
        Portfolio portfolio = portfolioRepository.findByUsername(username);
        if(portfolio !=null){
            Stock stock = stockRepository.findByUsernameAndSymbol(username, symbol);

            if (stock !=null)
                stock.setShares(stock.getShares()+shares);
            else {
                stock = new Stock();
                stock.setUsername(username);
                stock.setSymbol(symbol);
                stock.setShares(shares);
                Set<Stock> currentStock = portfolio.getStocksOwned();
                currentStock.add(stock);
            }

            stockRepository.save(stock);

            portfolioRepository.save(portfolio);
        }
        return portfolio;
    }

//    @GetMapping("{username}/returns/")
//    public BigDecimal getPorfolioReturns(@PathVariable("username") final String username){
//
//    }
}
