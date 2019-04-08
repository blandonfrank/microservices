package com.redpony.portfolioservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "portfolios")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor
public class Portfolio extends AbstractEntity {
    @NotBlank
    @NonNull
    private String username;
    private String owner;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal cash = BigDecimal.ZERO;
    private String sentiment;
    private BigDecimal performance = BigDecimal.ZERO;
    private double risk;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stock> stocks;



}
