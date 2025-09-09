package com.nwg.ezpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles all exceptions globally across controllers and services.
 * Provides meaningful messages instead of just status codes.
 *
 * Author: Muskan
 * Version: 1.0
 * Since: 2025-08-25
 * Revised: 2025-08-26 - included ResourceNotFoundException handling
 * Revised: 2025-09-06 - every exception response to return uniform stubbing (map).
 */
/**
 * Global exception handler for the banking application.
 * Provides centralized handling of exceptions across all controllers.
 */
@ControllerAdvice
public class BankingGlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} thrown from services or controllers.
     *
     * @param ex the {@link ResourceNotFoundException}
     * @return ResponseEntity containing error details (timestamp, message, status)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link InsufficientBalanceException} thrown during transactions.
     *
     * @param ex the {@link InsufficientBalanceException}
     * @return ResponseEntity containing the error message and HTTP 400 status
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalance(InsufficientBalanceException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unhandled exceptions globally.
     *
     * @param ex the {@link Exception}
     * @return ResponseEntity containing error details (timestamp, message, status)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", "An unexpected error occurred: " + ex.getMessage());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}