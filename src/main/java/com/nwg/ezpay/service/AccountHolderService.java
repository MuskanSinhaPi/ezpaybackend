package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.exception.ResourceNotFoundException;
import com.nwg.ezpay.repository.AccountHolderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing AccountHolder operations.
 * Allows realistic operations like fetching account holders and updating limited fields.
 *
 * Author: Muskan
 * Version: 1.4
 * Since: 2025-08-25
 * Revised: 2025-08-26 - restricted update to realistic fields only
 */
@Service
public class AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;

    public AccountHolderService(AccountHolderRepository accountHolderRepository) {
        this.accountHolderRepository = accountHolderRepository;
    }

    /**
     * Retrieve all account holders from the database.
     *
     * @return a list of {@link AccountHolder} objects; empty list if none exist
     */
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }

    /**
     * Fetch an {@link AccountHolder} by its unique ID.
     *
     * @param id the unique identifier of the account holder
     * @return the {@link AccountHolder} object corresponding to the given ID
     * @throws ResourceNotFoundException if no account holder exists with the given ID
     */
    public AccountHolder getAccountHolderById(Long id) {
        return accountHolderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AccountHolder not found with ID: " + id));
    }

    /**
     * Add a new {@link AccountHolder} to the database.
     *
     * @param accountHolder the {@link AccountHolder} entity to save
     * @return the saved {@link AccountHolder} object with generated ID
     */
    public AccountHolder addAccountHolder(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    /**
     * Fetch an {@link AccountHolder} by its unique username.
     * <p>
     * This is useful when the username is received from an external login system
     * and you need to map it to your internal account holder records.
     * </p>
     *
     * @param username the unique username of the account holder
     * @return the {@link AccountHolder} object corresponding to the given username
     * @throws ResourceNotFoundException if no account holder exists with the given username
     */
    public AccountHolder getByUsername(String username) {
        return accountHolderRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("AccountHolder not found for username: " + username));
    }


    /**
     * Update only the email of the account holder.
     *
     * @param id unique account holder ID
     * @param newEmail new email to update
     * @return updated AccountHolder
     * @throws ResourceNotFoundException if account holder not found
     */
    public AccountHolder updateEmail(Long id, String newEmail) {
        AccountHolder existing = getAccountHolderById(id);
        existing.setEmail(newEmail);
        return accountHolderRepository.save(existing);
    }

    /**
     * Update only the mobile number of the account holder.
     *
     * @param id unique account holder ID
     * @param newMobileNumber new mobile number to update
     * @return updated AccountHolder
     * @throws ResourceNotFoundException if account holder not found
     */
    public AccountHolder updateMobileNumber(Long id, String newMobileNumber) {
        AccountHolder existing = getAccountHolderById(id);
        existing.setMobileNumber(newMobileNumber);
        return accountHolderRepository.save(existing);
    }

    /**
     * Update the balance of an account holder.
     * <p>
     * This method is intentionally package-private to prevent external misuse.
     * Only other services (e.g., PaymentInstructionService) should call this
     * when processing transactions.
     *
     * @param accountHolder the account holder whose balance needs to be updated
     * @param newBalance    the new balance value to set
     * @return updated AccountHolder entity
     */
    AccountHolder updateBalanceInternal(AccountHolder accountHolder, Double newBalance) {
        accountHolder.setBalance(newBalance);
        return accountHolderRepository.save(accountHolder);
    }

    /**
     * Deletes an {@link AccountHolder} entity from the system.
     * <p>
     * This method first retrieves the account holder by the given ID. 
     * If the account holder exists, it will be removed from the repository.
     * </p>
     *
     * @param id the unique identifier of the {@code AccountHolder} to delete
     * @throws ResourceNotFoundException if no account holder is found with the given ID
     */
    public void deleteAccountHolder(Long id) {
        AccountHolder existing = getAccountHolderById(id);
        accountHolderRepository.delete(existing);
    }

}
