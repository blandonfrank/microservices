package com.redpony.transactionsservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;

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
    private String userName;
    private String owner;
    @Column(name = "trans_amount")
    private BigDecimal amount = BigDecimal.ZERO;
    private TransactionType transactionType;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "en_us")
    @Column(name = "trans_date", nullable = false, updatable = true)
    @CreationTimestamp
    private Date date;

    private BigDecimal commission = BigDecimal.ZERO;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Stock stock;


}