package com.redpony.transactionsservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    Long id;
    private String username;
    private String owner;
    @Embedded
    @Column(name = "trans_amount")
    private Money total;
    private TransactionType transactionType;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "en_us")
    @Column(name = "trans_date", nullable = false, updatable = true)
    @CreationTimestamp
    private Date date;

    private BigDecimal commission = BigDecimal.ZERO;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Stock stock;

    @Enumerated(value = EnumType.STRING)
    private TransactionState transactionState = TransactionState.PENDING;


}
