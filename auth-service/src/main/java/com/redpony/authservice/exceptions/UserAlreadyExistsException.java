package com.redpony.authservice.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAlreadyExistsException extends RuntimeException {
    String message;
}
