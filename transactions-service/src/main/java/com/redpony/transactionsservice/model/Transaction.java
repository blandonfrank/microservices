package com.redpony.transactionsservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(name = "transactions")
public class Transaction {
    @JsonIgnore
    @Id
    @GeneratedValue
    Long id;
    private String userName;
    private String owner;
    @Column(name = "trans_amount")
    private BigDecimal amount = BigDecimal.ZERO;
    private TransactionType transactionType;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "trans_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date date;

    private BigDecimal commission = BigDecimal.ZERO;
    @OneToOne
    private Stock stock;


}
