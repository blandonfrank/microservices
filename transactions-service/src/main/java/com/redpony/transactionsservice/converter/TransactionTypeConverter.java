package com.redpony.transactionsservice.converter;

import com.redpony.transactionsservice.model.TransactionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class TransactionTypeConverter implements Converter<String, TransactionType> {
    @Override
    public TransactionType convert(String value){
        return TransactionType.valueOf(value);
    }
}
