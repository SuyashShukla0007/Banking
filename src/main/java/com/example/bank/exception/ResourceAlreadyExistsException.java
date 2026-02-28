package com.example.bank.exception;

/**
 * Thrown when attempting to create a resource that already exists (duplicate
 * account, branch code, etc.).
 * Maps to HTTP 409 Conflict.
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
