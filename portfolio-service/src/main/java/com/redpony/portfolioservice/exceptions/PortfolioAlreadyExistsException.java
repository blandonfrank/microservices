package com.redpony.portfolioservice.exceptions;

public class PortfolioAlreadyExistsException extends Exception {
    String message;

    public PortfolioAlreadyExistsException(String message){
        super(message);
    }
}
