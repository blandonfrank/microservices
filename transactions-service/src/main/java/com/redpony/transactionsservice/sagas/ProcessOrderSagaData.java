package com.redpony.transactionsservice.sagas;

import com.redpony.transactionsservice.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ProcessOrderSagaData {
    private Transaction transaction;

}