package com.example.bank.exception;

/**
 * Thrown when a withdrawal or transfer cannot be completed due to insufficient
 * funds.
 * Maps to HTTP 422 Unprocessable Entity.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
