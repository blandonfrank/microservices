package com.redpony.stocksserviceproducer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntradayStockInfo {
    @JsonProperty("1. open")
    private double open;
    @JsonProperty("2. high")
    private double high;
    @JsonProperty("3. low")
    private double low;
    @JsonProperty("4. close")
    private double close;
    @JsonProperty("5. volume")
    private double volume;

}
