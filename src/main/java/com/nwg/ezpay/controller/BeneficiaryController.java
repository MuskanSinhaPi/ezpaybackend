package com.nwg.ezpay.controller;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import com.nwg.ezpay.service.BeneficiaryService;
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
 * REST controller for Beneficiary entity.
 * Handles adding, fetching, deleting beneficiaries.
 * Updates are limited to realistic operations.
 *
 * Author: Muskan
 * Version: 1.1
 * Since: 2025-08-25
 * Revised: 2025-08-26 - ensured realistic operations
 */
@RestController
@RequestMapping("/api/beneficiaries")
@CrossOrigin(origins = "http://localhost:4200")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    private final AccountHolderService accountHolderService;

    public BeneficiaryController(BeneficiaryService beneficiaryService,
                                 AccountHolderService accountHolderService) {
        this.beneficiaryService = beneficiaryService;
        this.accountHolderService = accountHolderService;
    }

    /**
     * Get all beneficiaries.
     *
     * @return list of Beneficiaries
     */
    @GetMapping
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {
        return new ResponseEntity<>(beneficiaryService.getAllBeneficiaries(), HttpStatus.OK);
    }

    /**
     * Get a beneficiary by ID.
     *
     * @param id beneficiary ID
     * @return Beneficiary object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id) {
        return new ResponseEntity<>(beneficiaryService.getBeneficiaryById(id), HttpStatus.OK);
    }

    /**
     * Get all beneficiaries for a specific account holder.
     *
     * @param username username of account holder
     * @return list of Beneficiaries
     */
    @GetMapping("/account-holder")
    public ResponseEntity<List<Beneficiary>> getByAccountHolder(@RequestParam String username) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        return new ResponseEntity<>(beneficiaryService.getBeneficiariesByAccountHolder(holder), HttpStatus.OK);
    }

    /**
     * Add a new beneficiary for an account holder.
     *
     * @param username    username of account holder
     * @param beneficiary Beneficiary object to add
     * @return saved Beneficiary
     */
    @PostMapping("/account-holder")
    public ResponseEntity<Beneficiary> addBeneficiary(@RequestParam String username,
                                                      @RequestBody Beneficiary beneficiary) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        beneficiary.setAccountHolder(holder);
        return new ResponseEntity<>(beneficiaryService.saveBeneficiary(beneficiary), HttpStatus.CREATED);
    }

    /**
     * Update a beneficiary's details.
     *
     * @param username      username of account holder
     * @param beneficiaryId ID of the beneficiary to update
     * @param beneficiary   Beneficiary object with updated fields
     * @return updated Beneficiary
     */
    @PutMapping("/account-holder/beneficiary/{beneficiaryId}")
    public ResponseEntity<Beneficiary> updateBeneficiary(@RequestParam String username,
                                                         @PathVariable Long beneficiaryId,
                                                         @RequestBody Beneficiary beneficiary) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        Beneficiary updatedBeneficiary = beneficiaryService.updateBeneficiary(beneficiaryId, holder, beneficiary);
        return new ResponseEntity<>(updatedBeneficiary, HttpStatus.OK);
    }

    /**
     * Delete a beneficiary for an account holder.
     *
     * @param username      username of account holder
     * @param beneficiaryId beneficiary ID
     * @return success message
     */
    @DeleteMapping("/account-holder/beneficiary/{beneficiaryId}")
    public ResponseEntity<String> deleteBeneficiary(@RequestParam String username,
                                                    @PathVariable Long beneficiaryId) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        beneficiaryService.deleteBeneficiary(beneficiaryId, holder);
        return new ResponseEntity<>("Beneficiary deleted successfully", HttpStatus.OK);
    }

    /* ---------------- Authentication-based endpoints (commented out) ----------------
    
    @GetMapping("/account-holder")
    public ResponseEntity<List<Beneficiary>> getByAccountHolderAuth() {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        return new ResponseEntity<>(beneficiaryService.getBeneficiariesByAccountHolder(holder), HttpStatus.OK);
    }

    @PostMapping("/account-holder")
    public ResponseEntity<Beneficiary> addBeneficiaryAuth(@RequestBody Beneficiary beneficiary) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        beneficiary.setAccountHolder(holder);
        return new ResponseEntity<>(beneficiaryService.saveBeneficiary(beneficiary), HttpStatus.CREATED);
    }

    @PutMapping("/account-holder/beneficiary/{beneficiaryId}")
    public ResponseEntity<Beneficiary> updateBeneficiaryAuth(@PathVariable Long beneficiaryId,
                                                             @RequestBody Beneficiary beneficiary) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        Beneficiary updatedBeneficiary = beneficiaryService.updateBeneficiary(beneficiaryId, holder, beneficiary);
        return new ResponseEntity<>(updatedBeneficiary, HttpStatus.OK);
    }

    @DeleteMapping("/account-holder/beneficiary/{beneficiaryId}")
    public ResponseEntity<String> deleteBeneficiaryAuth(@PathVariable Long beneficiaryId) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        beneficiaryService.deleteBeneficiary(beneficiaryId, holder);
        return new ResponseEntity<>("Beneficiary deleted successfully", HttpStatus.OK);
    }

    ------------------------------------------------------------------------------- */
}
