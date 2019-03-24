package com.redpony.portfolioservice.listener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@NoArgsConstructor
public class TransactionMessageListener {

    public void receiveMessage(Map<String, String> message){
        log.info("Received Messsage! ");

        for (Map.Entry<String, String> entry : message.entrySet()) {
           log.info(entry.getKey() + ":" + entry.getValue());
        }
    }
}
