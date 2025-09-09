package com.nwg.ezpay.controller;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.service.AccountHolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for AccountHolder entity.
 * Exposes endpoints for realistic operations only.
 *
 * Author: Muskan
 * Version: 1.1
 * Since: 2025-08-25
 * Revised: 2025-08-26 - restricted update operations
 */
@RestController
@RequestMapping("/api/account-holders")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    public AccountHolderController(AccountHolderService accountHolderService) {
        this.accountHolderService = accountHolderService;
    }

    /**
     * Get account holder by username (test version using @RequestParam)
     *
     * @param username username of account holder
     * @return AccountHolder object
     */
    @GetMapping("/by-username/{username}")
    public ResponseEntity<AccountHolder> getAccountHolderByUsername(@RequestParam String username) {
        return new ResponseEntity<>(accountHolderService.getByUsername(username), HttpStatus.OK);
    }

    /*
    // Uncomment this later to use authentication instead of request param
    @GetMapping("/me")
    public ResponseEntity<AccountHolder> getAccountHolderByAuth() {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        return new ResponseEntity<>(holder, HttpStatus.OK);
    }
    */

    @GetMapping
    public ResponseEntity<List<AccountHolder>> getAllAccountHolders() {
        return new ResponseEntity<>(accountHolderService.getAllAccountHolders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountHolder> getAccountHolderById(@PathVariable Long id) {
        return new ResponseEntity<>(accountHolderService.getAccountHolderById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountHolder> addAccountHolder(@RequestBody AccountHolder accountHolder) {
        return new ResponseEntity<>(accountHolderService.addAccountHolder(accountHolder), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<AccountHolder> updateEmail(@PathVariable Long id, @RequestParam String newEmail) {
        return new ResponseEntity<>(accountHolderService.updateEmail(id, newEmail), HttpStatus.OK);
    }

    @PutMapping("/{id}/mobile")
    public ResponseEntity<AccountHolder> updateMobile(@PathVariable Long id, @RequestParam String newMobileNumber) {
        return new ResponseEntity<>(accountHolderService.updateMobileNumber(id, newMobileNumber), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccountHolder(@PathVariable Long id) {
        accountHolderService.deleteAccountHolder(id);
        return new ResponseEntity<>("AccountHolder deleted successfully", HttpStatus.OK);
    }

    /**
     * Get the current balance of an account holder
     *
     * @param id ID of the account holder
     * @return current balance
     */
    @GetMapping("/{id}/balance")
    public Double getBalance(@PathVariable Long id) {
        return accountHolderService.getAccountHolderById(id).getBalance();
    }

}