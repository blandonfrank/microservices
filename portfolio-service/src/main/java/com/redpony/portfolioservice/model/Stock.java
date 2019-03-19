package com.redpony.portfolioservice.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "stocks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Stock extends AbstractEntity {
    private String userName;
    private String symbol;
    private int shares;
    private BigDecimal price;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}
