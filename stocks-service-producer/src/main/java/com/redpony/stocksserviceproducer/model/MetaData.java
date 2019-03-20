package com.redpony.stocksserviceproducer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaData {
    @JsonProperty("1. Information")
    String information;
    @JsonProperty("2. Symbol")
    String symbol;
    @JsonProperty("3. Last Refreshed")
    String lastRefreshed;
    @JsonProperty("4. Interval")
    String interval;
    @JsonProperty("5. Output Size")
    String outputSize;
    @JsonProperty("6. Time Zone")
    String timeZone;
}
