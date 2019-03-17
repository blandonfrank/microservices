package com.redpony.portfolioservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "portfolios")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class Portfolio extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @NotBlank
    private String userName;
    private String owner;
    private BigDecimal total;
    private BigDecimal balance;
    private String sentiment;
    private BigDecimal performance;

    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stock stocks;



}
