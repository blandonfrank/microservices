package com.redpony.transactionsservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Stock {
    @Id
    @GeneratedValue
    Long id;
    private String symbol;
    private String name;
    private BigDecimal price;
    private Long shares;
}
