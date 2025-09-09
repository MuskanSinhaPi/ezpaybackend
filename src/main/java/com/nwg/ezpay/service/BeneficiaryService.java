package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import com.nwg.ezpay.exception.ResourceNotFoundException;
import com.nwg.ezpay.repository.BeneficiaryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class to manage Beneficiary operations such as
 * adding, fetching, updating, deleting beneficiaries, and validating
 * ownership by account holder.
 *
 * Author: Muskan
 * Version: 1.2
 * Since: 2025-08-25
 * Revised: 2025-08-26 - added ownership validation and Optional methods
 */
@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    /**
     * Constructor to inject BeneficiaryRepository dependency.
     *
     * @param beneficiaryRepository repository for Beneficiary
     */
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    /**
     * Fetch all beneficiaries from the database.
     *
     * @return list of Beneficiary objects
     */
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    /**
     * Fetch a beneficiary by its unique ID.
     *
     * @param id unique ID of the beneficiary
     * @return Beneficiary object
     * @throws ResourceNotFoundException if beneficiary not found
     */
    public Beneficiary getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with ID: " + id));
    }

    /**
     * Fetch all beneficiaries for a specific account holder.
     *
     * @param accountHolder owner account holder
     * @return list of Beneficiary objects
     */
    public List<Beneficiary> getBeneficiariesByAccountHolder(AccountHolder accountHolder) {
        return beneficiaryRepository.findByAccountHolder(accountHolder);
    }

    /**
     * Fetch a specific beneficiary by ID and account holder.
     * Useful for validating ownership before updating or deleting.
     *
     * @param beneficiaryId unique beneficiary ID
     * @param accountHolder owner account holder
     * @return Beneficiary object
     * @throws ResourceNotFoundException if beneficiary not found for this account holder
     */
    public Beneficiary getBeneficiaryByIdAndAccountHolder(Long beneficiaryId, AccountHolder accountHolder) {
        Optional<Beneficiary> optionalBeneficiary =
                beneficiaryRepository.findByIdAndAccountHolder(beneficiaryId, accountHolder);
        return optionalBeneficiary.orElseThrow(
                () -> new ResourceNotFoundException("Beneficiary ID " + beneficiaryId +
                        " not found for AccountHolder ID: " + accountHolder.getId()));
    }

    /**
     * Add or update a beneficiary in the database.
     *
     * @param beneficiary Beneficiary object to save
     * @return saved Beneficiary object
     */
    public Beneficiary saveBeneficiary(Beneficiary beneficiary) {
        return beneficiaryRepository.save(beneficiary);
    }
    
    /**
     * Updates an existing beneficiary linked to an account holder.
     *
     * @param beneficiaryId ID of the beneficiary to update
     * @param accountHolder account holder who owns this beneficiary
     * @param updatedBeneficiary object containing updated fields
     * @return the updated Beneficiary object
     * @throws ResourceNotFoundException if beneficiary not found
     */
    public Beneficiary updateBeneficiary(Long beneficiaryId, AccountHolder accountHolder, Beneficiary updatedBeneficiary) {
        Beneficiary existing = beneficiaryRepository
            .findByIdAndAccountHolder(beneficiaryId, accountHolder)
            .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found for this account holder"));
        
        if (updatedBeneficiary.getName()!=null) {existing.setName(updatedBeneficiary.getName());}
        if (updatedBeneficiary.getAccountNumber()!=null) {existing.setAccountNumber(updatedBeneficiary.getAccountNumber());}
        if (updatedBeneficiary.getBankName()!=null) {existing.setBankName(updatedBeneficiary.getBankName());}
        if (updatedBeneficiary.getIfsc()!=null) {existing.setIfsc(updatedBeneficiary.getIfsc());}
        if (updatedBeneficiary.getEmail()!=null) {existing.setEmail(updatedBeneficiary.getEmail());}
        if (updatedBeneficiary.getName()!=null) {existing.setName(updatedBeneficiary.getName());}
        if (updatedBeneficiary.getPhone()!=null) {existing.setPhone(updatedBeneficiary.getPhone());}
        
        return beneficiaryRepository.save(existing);
    }

    /**
     * Delete a beneficiary by ID after validating ownership.
     *
     * @param beneficiaryId unique beneficiary ID
     * @param accountHolder owner of the beneficiary
     * @throws ResourceNotFoundException if beneficiary not found for this account holder
     */
    public void deleteBeneficiary(Long beneficiaryId, AccountHolder accountHolder) {
        Beneficiary existing = getBeneficiaryByIdAndAccountHolder(beneficiaryId, accountHolder);
        beneficiaryRepository.delete(existing);
    }
}
