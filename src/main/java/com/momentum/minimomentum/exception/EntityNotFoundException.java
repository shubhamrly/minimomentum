package com.momentum.minimomentum.exception;

/**
 * Custom exception to be thrown when an entity is not found in the database.
 * This exception extends RuntimeException and can be used to indicate
 * that a requested entity could not be located.
 */

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
