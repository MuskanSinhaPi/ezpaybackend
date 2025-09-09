package com.nwg.ezpay.exception;

/**
 * Exception thrown when an account is not found in the system.
 * 
 * @author Vaishali Aggarwal
 */
public class AccountNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AccountNotFoundException with the specified detail message.
     * 
     * @param message the detail message
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}