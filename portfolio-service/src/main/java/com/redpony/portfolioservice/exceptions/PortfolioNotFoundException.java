package com.redpony.portfolioservice.exceptions;

public class PortfolioNotFoundException extends Exception {
    String message;

    public PortfolioNotFoundException(String message){
        super(message);
    }
}
