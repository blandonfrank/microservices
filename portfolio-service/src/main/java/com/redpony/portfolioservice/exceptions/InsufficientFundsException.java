package com.redpony.portfolioservice.exceptions;

public class InsufficientFundsException extends RuntimeException {
    String message;

    public InsufficientFundsException(String message){
        super(message);
    }
}
