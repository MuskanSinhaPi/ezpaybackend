package com.nwg.ezpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nwg.ezpay.entity.UPIAccount;
import com.nwg.ezpay.service.UPIAccountService;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing UPI accounts.
 * Provides endpoints for CRUD operations on UPI accounts.
 * 
 * Author : Vaishali Aggarwal
 */
@RestController
@RequestMapping("/upi/accounts")
@CrossOrigin(origins = "http://localhost:4200")

public class UPIAccountController {

    /** Service layer for UPI account operations */
    @Autowired
    private UPIAccountService upiAccountService;

    /**
     * Constructor for dependency injection
     * 
     * @param upiAccountService the service to handle UPI account operations
     */
    public UPIAccountController(UPIAccountService upiAccountService) {
        super();
        this.upiAccountService = upiAccountService;
    }

    /**
     * Creates a new UPI account
     * 
     * @param upiAccount the account details to create
     * @return ResponseEntity containing the created account with HTTP 201 status
     */
    @PostMapping("/create")
    public ResponseEntity<UPIAccount> createAccount(
            @RequestBody UPIAccount upiAccount) {
        UPIAccount createdAccount = upiAccountService.createAccount(upiAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    /**
     * Retrieves all UPI accounts
     * 
     * @return ResponseEntity with list of all accounts, or no content if none exist
     */
    @GetMapping("/all")
    public ResponseEntity<List<UPIAccount>> getAllAccounts() {
        List<UPIAccount> accounts = upiAccountService.getAllAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

    /**
     * Retrieves a specific UPI account by its UPI ID
     * 
     * @param upiId the UPI ID to look up
     * @return ResponseEntity with the account if found, or 404 if not found
     */
    @GetMapping("/{upiId}")
    public ResponseEntity<UPIAccount> getAccount(@PathVariable String upiId) {
        Optional<UPIAccount> accountOpt = upiAccountService.getAccountByUpiId(upiId);
        return accountOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Updates the balance of a UPI account
     * 
     * @param upiId      the UPI ID of the account to update
     * @param newBalance the new balance to set
     * @return ResponseEntity with the updated account, or 404 if account not found
     */
    @PutMapping("/{upiId}/balance")
    public ResponseEntity<UPIAccount> updateBalance(
            @PathVariable String upiId,
            @RequestParam Integer newBalance) {
        try {
            UPIAccount updatedAccount = upiAccountService.updateBalance(upiId, newBalance);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes a UPI account
     * 
     * @param upiId the UPI ID of the account to delete
     * @return ResponseEntity with success message, or 404 if account not found
     */
    @DeleteMapping("/{upiId}")
    public ResponseEntity<String> deleteAccount(@PathVariable String upiId) {
        Optional<UPIAccount> accountOpt = upiAccountService.getAccountByUpiId(upiId);
        if (accountOpt.isPresent()) {
            upiAccountService.deleteAccount(upiId);
            return ResponseEntity.ok("Account with UPI ID " + upiId + " deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Account with UPI ID " + upiId + " not found.");
    }
}
