package com.nwg.ezpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for EZPay application.
 * Handles custom exceptions and returns appropriate HTTP responses.
 * 
 * @author Vaishali Aggarwal
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles InvalidPinException.
     * @param ex the exception
     * @return UNAUTHORIZED response with exception message
     */
    @ExceptionHandler(InvalidPinException.class)
    public ResponseEntity<String> handleInvalidPinException(InvalidPinException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    /**
     * Handles InsufficientBalanceException.
     * @param ex the exception
     * @return BAD_REQUEST response with exception message
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Handles InvalidAmountException.
     * @param ex the exception
     * @return BAD_REQUEST response with exception message
     */
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmountException(InvalidAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Handles AccountNotFoundException.
     * @param ex the exception
     * @return NOT_FOUND response with exception message
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles TransactionNotFoundException.
     * @param ex the exception
     * @return NOT_FOUND response with exception message
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}