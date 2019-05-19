package com.redpony.transactionsservice.events;

import com.redpony.transactionsservice.model.Transaction;
import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PortfolioCreditReservedEvent implements DomainEvent {
    private Transaction transaction;
}
