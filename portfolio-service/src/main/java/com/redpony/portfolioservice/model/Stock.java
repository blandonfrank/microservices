package com.redpony.portfolioservice.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "stocks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Stock extends AbstractEntity {
    private String symbol;
    private int shares;
    private BigDecimal totalReturn = BigDecimal.ZERO;
    private BigDecimal averageCost = BigDecimal.ZERO;

}
