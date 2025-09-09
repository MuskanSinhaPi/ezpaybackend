package com.nwg.ezpay.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nwg.ezpay.service.UPITransactionService;
import com.nwg.ezpay.entity.UPITransaction;
import com.nwg.ezpay.exception.AccountNotFoundException;
import com.nwg.ezpay.exception.InsufficientBalanceException;
import com.nwg.ezpay.exception.InvalidPinException;
import com.nwg.ezpay.exception.TransactionNotFoundException;
import com.nwg.ezpay.exception.InvalidAmountException;


/**
 * REST Controller for managing UPI transactions.
 * Provides endpoints for creating, reading, updating, and deleting UPI
 * transactions.
 * 
 * Author : Vaishali Aggarwal
 */
@RestController
@RequestMapping("upi/transactions")
@CrossOrigin(origins = "http://localhost:4200")

public class UPITransactionController {

    /** Service layer for UPI transaction operations */
    private final UPITransactionService upiTransactionService;

    /**
     * Constructor for dependency injection
     * 
     * @param upiTransactionService the service to handle UPI transaction operations
     */
    public UPITransactionController(UPITransactionService upiTransactionService) {
        super();
        this.upiTransactionService = upiTransactionService;
    }

    /**
     * Retrieves all UPI transactions
     * 
     * @return ResponseEntity with list of all transactions, or no content if none
     *         exist
     */
    @GetMapping("/all")
    public ResponseEntity<List<UPITransaction>> showAllUPITransactions() {
        List<UPITransaction> transactions = upiTransactionService.showAllUPITransactions();
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    /**
     * Creates a new UPI transaction
     * 
     * @param upiTransaction the transaction details to create
     * @return ResponseEntity containing the created transaction with HTTP 201
     *         status
     */
    @PostMapping("/create")
    public ResponseEntity<UPITransaction> createTransaction(@RequestBody UPITransaction upiTransaction) {
        UPITransaction createdTransaction = upiTransactionService.addUPITransaction(upiTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    /**
     * Verifies a transaction using PIN
     * 
     * @param id  the transaction ID to verify
     * @param pin the PIN to verify against
     * @return ResponseEntity with verified transaction or appropriate error status
     *         404 if transaction not found
     *         409 if transaction state conflicts
     *         400 if invalid parameters
     *         500 for unexpected errors
     */
    @PostMapping("/{id}/verify")
    public ResponseEntity<?> verifyTransaction(@PathVariable Integer id, @RequestParam String pin) {
        try {
            UPITransaction verifiedTransaction = upiTransactionService.verifyTransactionPin(id, pin);
            return ResponseEntity.ok(verifiedTransaction);
        } catch (TransactionNotFoundException | AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidPinException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (InsufficientBalanceException | InvalidAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Deletes a UPI transaction
     * 
     * @param id the transaction ID to delete
     * @return ResponseEntity with success message, or 404 if transaction not found
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable int id) {
        boolean deleted = upiTransactionService.deleteUPITransaction(id);
        if (deleted) {
            return ResponseEntity.ok("Transaction with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transaction with ID " + id + " not found.");
        }
    }

    /**
     * Retrieves a specific transaction by ID
     * 
     * @param id the transaction ID to look up
     * @return ResponseEntity with the transaction if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UPITransaction> getTransactionById(@PathVariable int id) {
        return upiTransactionService.getUPITransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieves all transactions with a specific status
     * 
     * @param status the status to filter by
     * @return ResponseEntity with list of matching transactions, or no content if
     *         none exist
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UPITransaction>> getTransactionsByStatus(@PathVariable String status) {
        List<UPITransaction> transactions = upiTransactionService.getUPITransactionsByStatus(status);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * Retrieves all transactions with specific UPI ID
     * 
     * @param upiId the Upi to filter by
     * @return ResponseEntity with list of matching transactions, or no content if
     *         none exist
     */
    @GetMapping("/upiId/{upiId}")
    public ResponseEntity<List<UPITransaction>> getTransactionsByUpiId(@PathVariable String upiId) {
        List<UPITransaction> transactions = upiTransactionService.getUPITransactionsByUpiId(upiId);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }
}
