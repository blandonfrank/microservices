package com.redpony.accountservice.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountAlreadyExistsException extends RuntimeException {
    String message;
}
