package com.redpony.portfolioservice.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Stock{
    private String symbol;
    private Long shares;
    private BigDecimal totalReturn = BigDecimal.ZERO;
    private BigDecimal averageCost = BigDecimal.ZERO;
    private BigDecimal totalCost = BigDecimal.ZERO;

    public Stock(String symbol){
        this.symbol = symbol;
    }

}
