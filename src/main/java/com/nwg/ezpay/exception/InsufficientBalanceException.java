package com.nwg.ezpay.exception;

import java.io.Serializable;

/**
 * Exception thrown when an account does not have sufficient balance
 * to complete a transaction.
 *
 * @author Muskan
 * @version 1.0
 * @since 2025-09-01
 */
public class InsufficientBalanceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L; // Explicitly added

    /**
     * Constructs a new InsufficientBalanceException with the specified detail message.
     *
     * @param message the detail message
     */
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
