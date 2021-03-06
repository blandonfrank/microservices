package com.redpony.portfolioservice.service;

import com.redpony.portfolioservice.exceptions.PortfolioAlreadyExistsException;
import com.redpony.portfolioservice.exceptions.PortfolioNotFoundException;
import com.redpony.portfolioservice.model.Portfolio;
import com.redpony.portfolioservice.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<Portfolio> getAllPortfolios(){
        return portfolioRepository.findAll();
    }


    public Portfolio getPortfolioById(int id) throws PortfolioNotFoundException {
        if(!portfolioExists(id))
            throw new PortfolioNotFoundException("Portfolio not found");
        return portfolioRepository.findById(id).get();
    }
    public Portfolio getPortfolioByUsername(String username) throws PortfolioNotFoundException {
        if(!portfolioExists(username))
            throw new PortfolioNotFoundException("Portfolio not found");
        return portfolioRepository.findByUsername(username);
    }

    @Transactional
    public Portfolio createPorfolio(Portfolio portfolio) throws PortfolioAlreadyExistsException{
        if(portfolioExists(portfolio.getUsername()))
            throw new PortfolioAlreadyExistsException("Portfolio for this user already exists");

        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio updatePortfolio(Portfolio portfolio) throws PortfolioNotFoundException{
        if(!portfolioExists(portfolio.getUsername()))
            throw new PortfolioNotFoundException("Portfolio not found");

        Portfolio portfolioToUpdate = portfolioRepository.getOne(portfolio.getId());

        portfolioToUpdate.setCash(portfolio.getCash());
//        if(portfolio.getStocks().size() > 0)
//            portfolioToUpdate.setStocks(portfolio.getStocks());

        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public void deletePortfolio(int id) throws PortfolioNotFoundException{
        if(!portfolioExists(id))
            throw new PortfolioNotFoundException("Portfolio not found");

        portfolioRepository.deleteById(id);
    }

    @Transactional
    public void deletePortfolio(String username) throws PortfolioNotFoundException{
        if(!portfolioExists(username))
            throw new PortfolioNotFoundException("Portfolio not found");

        portfolioRepository.deleteByUsername(username);
    }

    public BigDecimal getPorfolioReturns(final String username){
        BigDecimal performance = BigDecimal.ZERO;
        if(portfolioExists(username)) {
            Portfolio portfolio = portfolioRepository.findByUsername(username);
            performance.add(portfolio.getPerformance());
        }
        return performance;
    }

    public BigDecimal getPorfolioBalance(final String username){
        BigDecimal balance = BigDecimal.ZERO;
        if(portfolioExists(username)) {
            Portfolio portfolio = portfolioRepository.findByUsername(username);
            balance.add(portfolio.getPerformance());
        }
        return balance;
    }


    public boolean portfolioExists(String username){
        return portfolioRepository.existsByUsername(username);
    }

    public boolean portfolioExists(Integer id){
        return portfolioRepository.existsById(id);
    }
}
