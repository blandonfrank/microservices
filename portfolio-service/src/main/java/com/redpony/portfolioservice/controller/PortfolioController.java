package com.redpony.portfolioservice.controller;

import com.redpony.portfolioservice.model.Portfolio;
import com.redpony.portfolioservice.service.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.websocket.server.PathParam;

@RestController
@Slf4j
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;

    @GetMapping("/{username}")
    public Portfolio getPortfolio(@PathVariable("username") final String username){
        return portfolioService.getPortfolio(username);
    }

    @PostMapping("/create/{username}")
    @Transactional
    public Portfolio createPorfolio(@PathVariable("username") final String username){
       return portfolioService.createPorfolio(username);
    }

    @PutMapping("/update/{username}")
    public Portfolio updatePortfolio(@PathVariable("username") final String username, @PathParam("symbol") final String symbol, @PathParam("shares") int shares){
        return portfolioService.updatePortfolio(username,symbol,shares);
    }
}
