package com.redpony.portfolioservice.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "stocks")
@Setter @NoArgsConstructor @AllArgsConstructor
public class Stock extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;
    private int shares;
    private BigDecimal price;
    private BigDecimal total;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToMany(mappedBy = "stocks", cascade = CascadeType.ALL)
    private Set<Portfolio> portfolios;
}
