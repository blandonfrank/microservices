package com.redpony.portfolioservice.controller;

import com.redpony.portfolioservice.exceptions.PortfolioAlreadyExistsException;
import com.redpony.portfolioservice.exceptions.PortfolioNotFoundException;
import com.redpony.portfolioservice.model.Portfolio;
import com.redpony.portfolioservice.service.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;

    //Once authentication is added, only allow admins to call this
    @GetMapping("/portfolios")
    public List<Portfolio> getAllPortfolios(){
        return portfolioService.getAllPortfolios();
    }


    @GetMapping("/portfolio/{id}")
    public Portfolio getPortfolio(@PathVariable("id") final int id) throws PortfolioNotFoundException {
        return portfolioService.getPortfolioById(id);
    }

    @GetMapping("/portfolio/user/{username}")
    public Portfolio getPortfolioByUser(@PathVariable("username") final String username) throws PortfolioNotFoundException {
        return portfolioService.getPortfolioByUsername(username);
    }

    @PostMapping("/portfolio")
    @Transactional
    public ResponseEntity<Portfolio> createPorfolio(@Valid @RequestBody Portfolio portfolio) throws PortfolioAlreadyExistsException{
       return ResponseEntity.ok().body(portfolioService.createPorfolio(portfolio));
    }

    @PutMapping("/portfolio")
    @Transactional
    public ResponseEntity<Portfolio> updatePortfolio(@Valid @RequestBody Portfolio portfolio) throws PortfolioNotFoundException {
        return ResponseEntity.ok().body(portfolioService.updatePortfolio(portfolio));
    }

    @DeleteMapping("/portfolio/{id}")
    public ResponseEntity<?> deletePortfolio(@PathVariable("id") int id) throws PortfolioNotFoundException {
        log.info("Request to delete portfolio: {}", id);
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/portfolio/user/{username}")
    public ResponseEntity<?> deletePortfolio(@PathVariable("username") String username) throws PortfolioNotFoundException {
        log.info("Request to delete portfolio(s): {}", username);
        portfolioService.deletePortfolio(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/portfolio/{username}/returns")
    public BigDecimal getPorfolioReturns(@PathVariable("username") final String username) {
        return portfolioService.getPorfolioReturns(username);
    }

    @GetMapping("portfolio/{username}/balance")
    public BigDecimal getPortfolioBalance(@PathVariable("username") final String username){
        return portfolioService.getPorfolioBalance(username);
    }

    @ExceptionHandler( {PortfolioAlreadyExistsException.class} )
    public ResponseEntity<String> handleAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler( {PortfolioNotFoundException.class} )
    public ResponseEntity<String> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
