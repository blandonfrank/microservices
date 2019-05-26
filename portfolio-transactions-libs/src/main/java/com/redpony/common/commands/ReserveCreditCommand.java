package com.redpony.common.commands;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ReserveCreditCommand implements Command {
    private Long transId;
    private String userName;
    private BigDecimal amount;
    private String symbol;
    private Long shares;
    private BigDecimal price;
}