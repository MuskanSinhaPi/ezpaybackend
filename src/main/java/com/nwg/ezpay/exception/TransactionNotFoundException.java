package com.nwg.ezpay.exception;

/**
 * Exception thrown when a transaction is not found.
 * 
 * @author Vaishali Aggarwal
 */
public class TransactionNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new TransactionNotFoundException with the specified detail message.
     * @param message the detail message
     */
    public TransactionNotFoundException(String message) {
        super(message);
    }
}