package com.redpony.stocksserviceproducer.producer;

import com.redpony.stocksserviceproducer.model.StockResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Producer {

    private static final String TOPIC = "stocksTopic";

    @Autowired
    private KafkaTemplate<String, StockResponse> kafkaTemplate;

    public void sendMessage(StockResponse message){
        log.info("Sending message " + message.toString());
        this.kafkaTemplate.send(TOPIC, message);
    }

}
