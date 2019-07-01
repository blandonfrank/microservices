package com.redpony.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "accounts")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Account {
    @Id
    private String username;
    private String ownerName;
    private Date lastSeen;
}
