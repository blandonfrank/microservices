package com.redpony.transactionsservice.service;

import com.redpony.transactionsservice.events.PorfolioCreditReservationFailedEvent;
import com.redpony.transactionsservice.events.PortfolioCreditReservedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class PortfolioEventConsumer {
    @Autowired
    TransactionService transactionService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.redpony.portfolioservices.model.portfolio")
                .onEvent(PortfolioCreditReservedEvent.class, this::portfolioCreditReservedEventHandler)
                .onEvent(PorfolioCreditReservationFailedEvent.class, this::portfolioCreditReservationFailedEventHandler)
                .build();
    }

    private void portfolioCreditReservedEventHandler(DomainEventEnvelope<PortfolioCreditReservedEvent> domainEventEnvelope) {
        transactionService.approveTransaction(domainEventEnvelope.getEvent().getTransaction().getId());
    }

    private void portfolioCreditReservationFailedEventHandler(DomainEventEnvelope<PorfolioCreditReservationFailedEvent> domainEventEnvelope) {
        transactionService.rejectTransaction(domainEventEnvelope.getEvent().getTransaction().getId());
    }
}
