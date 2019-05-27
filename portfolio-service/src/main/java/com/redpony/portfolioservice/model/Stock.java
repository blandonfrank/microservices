package com.redpony.portfolioservice.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Stock{
    private String symbol;
    private Long shares;
    @Column(precision = 11, scale = 2)
    private BigDecimal totalReturn = BigDecimal.ZERO;
    @Column(precision = 11, scale = 2)
    private BigDecimal averageCost = BigDecimal.ZERO;
    @Column(precision = 11, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;

    public Stock(String symbol){
        this.symbol = symbol;
    }

}
