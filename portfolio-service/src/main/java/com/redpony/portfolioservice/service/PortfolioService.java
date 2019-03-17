package com.redpony.portfolioservice.service;

import com.redpony.portfolioservice.model.Portfolio;
import com.redpony.portfolioservice.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/portfolio")
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    //Once authentication is added, only allow admins to call this
    @GetMapping("/")
    public List<Portfolio> getPortfolios(){
        return portfolioRepository.findAll();
    }


    @GetMapping("/{username}")
    public Portfolio getPortfolio(@PathVariable("username") final String username){
        Portfolio portfolio = portfolioRepository.findByUserName(username);

        log.info("Looking up portfolio for " + username);
        if(Objects.isNull(portfolio))
            return new Portfolio();

        return portfolio;
    }

    @PostMapping("/create/{username}")
    public Portfolio createPorfolio(@PathVariable("username") final String username){
        Portfolio portfolio = new Portfolio();
        if(!StringUtils.isEmpty(username)){
            if(portfolioRepository.findByUserName(username) !=null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Portfolio already exists for: " + username );

            portfolio.setUserName(username);
            portfolioRepository.save(portfolio);
        }
        return portfolio;
    }

//    @GetMapping("{username}/returns/")
//    public BigDecimal getPorfolioReturns(@PathVariable("username") final String username){
//
//    }
}
