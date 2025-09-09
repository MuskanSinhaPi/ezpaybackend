package com.nwg.ezpay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwg.ezpay.entity.UPIAccount;
import com.nwg.ezpay.repository.UPIAccountRepository;

import java.util.List;
import java.util.Optional;

/**
 * {@code UPIAccountService} is a service layer component responsible for 
 * managing all business logic related to UPI account operations. 
 * <p>
 * This class provides methods to:
 * <ul>
 *   <li>Create new UPI accounts</li>
 *   <li>Retrieve existing UPI accounts (all or by UPI ID)</li>
 *   <li>Update account balances</li>
 *   <li>Delete accounts when no longer needed</li>
 * </ul>
 * <p>
 * It interacts directly with {@link UPIAccountRepository} for data persistence, 
 * abstracting away database operations from higher-level layers such as 
 * controllers. This ensures a clean separation of concerns between 
 * business logic and data access logic.
 * </p>
 *
 * Typical usage:
 * <pre>
 * {@code
 * UPIAccount account = new UPIAccount("aziz@upi", 5000);
 * upiAccountService.createAccount(account);
 *
 * Optional<UPIAccount> existing = upiAccountService.getAccountByUpiId("aziz@upi");
 * if (existing.isPresent()) {
 *     upiAccountService.updateBalance("aziz@upi", 6000);
 * }
 * }
 * </pre>
 * 
 * @author: Vaishali Aggarwal
 */
@Service
public class UPIAccountService {

    /** 
     * Repository instance for UPI account persistence and retrieval operations. 
     * Injected automatically by Spring via {@link Autowired}.
     */
    @Autowired
    private UPIAccountRepository upiAccountRepository;

    /**
     * Constructs a {@code UPIAccountService} with an injected repository.
     * <p>
     * This constructor is primarily useful for unit testing where a mock
     * repository can be passed in.
     *
     * @param upiAccountRepository repository implementation for account operations
     */
    public UPIAccountService(UPIAccountRepository upiAccountRepository) {
        super();
        this.upiAccountRepository = upiAccountRepository;
    }

    /**
     * Creates and persists a new UPI account in the system.
     * <p>
     * This method saves the provided {@link UPIAccount} entity into the database.
     * If an account with the same UPI ID already exists, the repository 
     * behavior (update/overwrite) will depend on JPA configuration and database
     * constraints.
     *
     * @param upiAccount the account entity containing UPI ID and initial balance
     * @return the saved UPI account instance (with generated ID if applicable)
     */
    public UPIAccount createAccount(UPIAccount upiAccount) {
        return upiAccountRepository.save(upiAccount);
    }

    /**
     * Fetches all UPI accounts currently available in the system.
     * <p>
     * Useful for administrative tasks such as listing all registered users 
     * or auditing balances. This method should be used cautiously in production 
     * for large datasets, as it retrieves all rows from the underlying table.
     *
     * @return a list containing all UPI accounts; may be empty if none exist
     */
    public List<UPIAccount> getAllAccounts() {
        return upiAccountRepository.findAll();
    }

    /**
     * Retrieves a specific UPI account using its unique UPI ID.
     * <p>
     * UPI ID is typically in the format {@code username@bank}. This method wraps 
     * the result in an {@link Optional} to avoid {@code null} returns.
     *
     * @param upiId the unique UPI ID to search for
     * @return an {@link Optional} containing the account if found, or empty if not found
     */
    public Optional<UPIAccount> getAccountByUpiId(String upiId) {
        return upiAccountRepository.findByUpiId(upiId);
    }

    /**
     * Updates the balance of an existing UPI account.
     * <p>
     * This method finds the account by UPI ID, updates its balance field, and 
     * persists the changes back into the database.
     * </p>
     * Edge cases:
     * <ul>
     *   <li>If the account does not exist, a {@link RuntimeException} is thrown.</li>
     *   <li>If the new balance is negative, no validation is performed here 
     *       (such validation should occur in higher layers if required).</li>
     * </ul>
     *
     * @param upiId      the UPI ID of the account to update
     * @param newBalance the new balance value to assign to the account
     * @return the updated UPI account entity reflecting the new balance
     * @throws RuntimeException if no account is found with the given UPI ID
     */
    public UPIAccount updateBalance(String upiId, Integer newBalance) {
        Optional<UPIAccount> accountOpt = upiAccountRepository.findByUpiId(upiId);
        if (accountOpt.isPresent()) {
            UPIAccount account = accountOpt.get();
            account.setBalance(newBalance);
            return upiAccountRepository.save(account);
        }
        throw new RuntimeException("Account not found for UPI ID: " + upiId);
    }

    /**
     * Deletes an existing UPI account identified by its UPI ID.
     * <p>
     * If the account exists, it is removed from the database. If not, 
     * the method completes silently without performing any action.
     * </p>
     * Typical use cases include:
     * <ul>
     *   <li>Deactivating user accounts</li>
     *   <li>Cleaning up test data</li>
     * </ul>
     *
     * @param upiId the UPI ID of the account to be deleted
     */
    public void deleteAccount(String upiId) {
        Optional<UPIAccount> accountOpt = upiAccountRepository.findByUpiId(upiId);
        accountOpt.ifPresent(upiAccountRepository::delete);
    }
}
