package com.nwg.ezpay.exception;

/**
 * Exception thrown when an entity is not found in the database.
 *
 * Author: Muskan
 * Version: 1.0
 * Since: 2025-08-24
 * Revised: 2025-08-24
 */
@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a message.
     *
     * @param message details about the missing resource
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
