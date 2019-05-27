package com.redpony.portfolioservice.exceptions;

public class InsufficientHoldingException extends RuntimeException {
    String message;

    public InsufficientHoldingException(String message){
        super(message);
    }
}
