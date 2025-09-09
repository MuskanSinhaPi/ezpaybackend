package com.nwg.ezpay.exception;

/**
 * Exception thrown when an invalid amount is provided.
 * 
 * @author Vaishali Aggarwal
 */
public class InvalidAmountException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidAmountException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}