package com.redpony.stocksserviceproducer.producer;

import com.redpony.stocksserviceproducer.model.StockResponse;
import lombok.extern.slf4j.Slf4j;
import org.patriques.output.timeseries.data.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Producer {

    private static final String TOPIC = "stocksTopic";

    @Autowired
    private KafkaTemplate<String, StockData> kafkaTemplate;

    public void sendMessage(StockData message){
        log.info("Sending message " + message.toString());
        this.kafkaTemplate.send(TOPIC, message);
    }

}
