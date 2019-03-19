package com.redpony.stocksserviceproducer.controllers;

import com.redpony.stocksserviceproducer.model.StockResponse;
import com.redpony.stocksserviceproducer.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

@RestController
@RequestMapping(value = "/kafka")
public class StockServiceController {

    private final Producer producer;

    @Autowired
    StockServiceController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("symbol") String symbol) {
        StockResponse stockResponse = new StockResponse();

        try {
            Stock stock = YahooFinance.get(symbol);
            stockResponse.setSymbol(stock.getSymbol());
            stockResponse.setPrice(stock.getQuote().getPrice());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.producer.sendMessage(stockResponse);
    }

}
