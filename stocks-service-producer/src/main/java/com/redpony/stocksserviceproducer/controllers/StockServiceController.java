package com.redpony.stocksserviceproducer.controllers;

import com.redpony.stocksserviceproducer.model.IntradayStockInfo;
import com.redpony.stocksserviceproducer.model.StockResponse;
import com.redpony.stocksserviceproducer.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/kafka")
public class StockServiceController {

    private final Producer producer;

    @Autowired
    StockServiceController(Producer producer) {
        this.producer = producer;
    }

    //9A3I9GAYSVM0DE6I

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("symbol") String symbol) {
        StockResponse stockResponse = new StockResponse();

        //try {

            String apiKey = "KEY";

            int timeout = 3000;

            AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey,timeout);
            TimeSeries stockTimeSeries = new TimeSeries(apiConnector);

            try{
                IntraDay response = stockTimeSeries.intraDay(symbol, Interval.ONE_MIN, OutputSize.COMPACT);

                List<StockData> stockData = response.getStockData();
                stockData.forEach(stock -> {
                    this.producer.sendMessage(stock);
                });

        } catch (AlphaVantageException e) {
            e.printStackTrace();
        }


    }


}
