package com.redpony.stocksserviceproducer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class TimeSeries {
    Map<String, IntradayStockInfo> dates;

}
