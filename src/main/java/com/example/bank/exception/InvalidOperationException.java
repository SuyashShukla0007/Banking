package com.example.bank.exception;

/**
 * Thrown when an operation is invalid due to the current state of a resource
 * (e.g., closing an already-closed account, approving a non-pending request).
 * Maps to HTTP 400 Bad Request.
 */
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
