package com.example.bank.exception;

/**
 * Thrown when a requested resource (account, branch, transaction, approval,
 * etc.) is not found.
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
