    package com.redpony.transactionsservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "trans_id", unique = true)
    Long transId;

    private String username;
    private String owner;

    @Column(name = "trans_amount")
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "en_us")
    @Column(name = "trans_date", nullable = false, updatable = true)
    @CreationTimestamp
    private Date date;

    private BigDecimal commission = BigDecimal.ZERO;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private Stock stock;

    @Enumerated(value = EnumType.STRING)
    private TransactionState state = TransactionState.PENDING;

    public void approved() {
        this.state = TransactionState.APPROVED;
    }

    public void rejected() {
        this.state = TransactionState.REJECTED;
    }

}
