package com.redpony.stocksserviceproducer.model;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class StockResponse {
    String symbol;
    BigDecimal price;
}
