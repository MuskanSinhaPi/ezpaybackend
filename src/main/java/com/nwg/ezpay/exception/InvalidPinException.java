package com.nwg.ezpay.exception;

/**
 * Exception thrown when an invalid PIN is provided.
 * 
 * @author Vaishali Aggarwal
 */
public class InvalidPinException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidPinException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidPinException(String message) {
        super(message);
    }
}