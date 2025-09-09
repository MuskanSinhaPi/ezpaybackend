package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.UPIAccount;
import com.nwg.ezpay.repository.UPIAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UPIAccountService}.
 *
 * This test class validates the behavior of UPIAccountService methods.
 * - Repository calls are mocked using Mockito so that database operations
 *   are simulated, not actually performed.
 * - Each test isolates one functionality of the service and ensures that
 *   correct interactions happen with the repository, correct values are returned,
 *   and correct exceptions are thrown when invalid inputs are provided.
 *
 * Author: Aditi Roy
 */
class UPIAccountServiceTest {

    // Mocked dependency (fake repository)
    @Mock
    private UPIAccountRepository upiAccountRepository;

    // Service under test, injected with the mocked repository
    @InjectMocks
    private UPIAccountService upiAccountService;

    // A reusable sample UPIAccount object for multiple tests
    private UPIAccount testAccount;

    /**
     * Runs before every test case.
     * - Initializes Mockito annotations so mocks are created and injected.
     * - Creates a sample UPIAccount with id, upiId, and balance values.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testAccount = new UPIAccount();
        testAccount.setId(1L);
        testAccount.setUpiId(" john@upi");
        testAccount.setBalance(5000);
    }

    // =============================================================
    // CREATE ACCOUNT TESTS
    // =============================================================

    /**
     * Scenario: Successfully creating a UPIAccount.
     * Expected: The repository saves the account and returns the same object.
     */
    @Test
    void testCreateAccount_Success() {
        // Mock behavior: repository will return the same account when save() is called
        when(upiAccountRepository.save(testAccount)).thenReturn(testAccount);

        // Call service method
        UPIAccount saved = upiAccountService.createAccount(testAccount);

        // Verify outcomes
        assertNotNull(saved); // Returned object should not be null
        assertEquals(" john@upi", saved.getUpiId()); // UPI ID must match
        verify(upiAccountRepository, times(1)).save(testAccount); // Ensure repository save() called once
    }

    /**
     * Scenario: Passing null to createAccount().
     * Expected: Repository throws IllegalArgumentException.
     */
    @Test
    void testCreateAccount_NullObject() {
        // Mock: repository will throw exception if null is saved
        when(upiAccountRepository.save(null)).thenThrow(new IllegalArgumentException("Entity must not be null"));

        // Assert: service should also throw the same exception
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> upiAccountService.createAccount(null));

        assertEquals("Entity must not be null", ex.getMessage());
    }

    // =============================================================
    // GET ALL ACCOUNTS TESTS
    // =============================================================

    /**
     * Scenario: Database has 1 account.
     * Expected: Service returns a list containing that account.
     */
    @Test
    void testGetAllAccounts() {
        // Mock: repository returns a list with one account
        when(upiAccountRepository.findAll()).thenReturn(Arrays.asList(testAccount));

        // Call service method
        List<UPIAccount> accounts = upiAccountService.getAllAccounts();

        // Verify
        assertEquals(1, accounts.size()); // List must contain exactly 1 account
        assertEquals(" john@upi", accounts.get(0).getUpiId()); // UPI ID must match
        verify(upiAccountRepository, times(1)).findAll(); // Ensure findAll() is called once
    }

    /**
     * Scenario: Database has no accounts.
     * Expected: Service returns an empty list.
     */
    @Test
    void testGetAllAccounts_EmptyList() {
        // Mock: repository returns empty list
        when(upiAccountRepository.findAll()).thenReturn(Collections.emptyList());

        // Call service
        List<UPIAccount> accounts = upiAccountService.getAllAccounts();

        // Verify
        assertTrue(accounts.isEmpty()); // List should be empty
        verify(upiAccountRepository, times(1)).findAll(); // Ensure repository method was called
    }

    // =============================================================
    // GET ACCOUNT BY UPI ID TESTS
    // =============================================================

    /**
     * Scenario: Account exists for given UPI ID.
     * Expected: Service returns Optional containing account.
     */
    @Test
    void testGetAccountByUpiId_Found() {
        // Mock: repository finds the account
        when(upiAccountRepository.findByUpiId(" john@upi")).thenReturn(Optional.of(testAccount));

        // Call service
        Optional<UPIAccount> result = upiAccountService.getAccountByUpiId(" john@upi");

        // Verify
        assertTrue(result.isPresent()); // Optional should contain a value
        assertEquals(5000, result.get().getBalance()); // Balance should match
        verify(upiAccountRepository, times(1)).findByUpiId(" john@upi"); // Ensure repository called once
    }

    /**
     * Scenario: Account does not exist for given UPI ID.
     * Expected: Service returns an empty Optional.
     */
    @Test
    void testGetAccountByUpiId_NotFound() {
        // Mock: repository returns empty Optional
        when(upiAccountRepository.findByUpiId("xyz@upi")).thenReturn(Optional.empty());

        // Call service
        Optional<UPIAccount> result = upiAccountService.getAccountByUpiId("xyz@upi");

        // Verify
        assertFalse(result.isPresent()); // Should be empty
    }

    /**
     * Scenario: Passing null as UPI ID.
     * Expected: IllegalArgumentException is thrown.
     */
    @Test
    void testGetAccountByUpiId_NullId() {
        // Mock: repository throws exception for null input
        when(upiAccountRepository.findByUpiId(null)).thenThrow(new IllegalArgumentException("UPI ID cannot be null"));

        // Assert exception thrown by service
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> upiAccountService.getAccountByUpiId(null));

        assertEquals("UPI ID cannot be null", ex.getMessage());
    }

    // =============================================================
    // UPDATE BALANCE TESTS
    // =============================================================

    /**
     * Scenario: Update balance for an existing account.
     * Expected: New balance is saved and returned.
     */
    @Test
    void testUpdateBalance_Success() {
        // Mock: account exists
        when(upiAccountRepository.findByUpiId(" john@upi")).thenReturn(Optional.of(testAccount));
        // Mock: saving the updated account returns it
        when(upiAccountRepository.save(any(UPIAccount.class))).thenReturn(testAccount);

        // Call service to update balance
        UPIAccount updated = upiAccountService.updateBalance(" john@upi", 7000);

        // Verify
        assertEquals(7000, updated.getBalance()); // Balance should be updated
        verify(upiAccountRepository, times(1)).save(testAccount); // Ensure save() called
    }

    /**
     * Scenario: Trying to update balance for non-existing account.
     * Expected: RuntimeException with "Account not found" message.
     */
    @Test
    void testUpdateBalance_NotFound() {
        // Mock: account not found
        when(upiAccountRepository.findByUpiId("xyz@upi")).thenReturn(Optional.empty());

        // Assert exception thrown
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> upiAccountService.updateBalance("xyz@upi", 7000));

        assertEquals("Account not found for UPI ID: xyz@upi", ex.getMessage());
    }

    /**
     * Scenario: Null UPI ID provided.
     * Expected: RuntimeException with "Account not found" message.
     */
    @Test
    void testUpdateBalance_NullId() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> upiAccountService.updateBalance(null, 500));

        assertEquals("Account not found for UPI ID: null", ex.getMessage());
    }

    // =============================================================
    // DELETE ACCOUNT TESTS
    // =============================================================

    /**
     * Scenario: Deleting account that exists.
     * Expected: Repository delete() is called with the account.
     */
    @Test
    void testDeleteAccount_Found() {
        // Mock: account exists
        when(upiAccountRepository.findByUpiId(" john@upi")).thenReturn(Optional.of(testAccount));

        // Call service delete
        upiAccountService.deleteAccount(" john@upi");

        // Verify repository delete() called with the account
        verify(upiAccountRepository, times(1)).delete(testAccount);
    }

    /**
     * Scenario: Deleting account that does not exist.
     * Expected: Repository delete() is never called.
     */
    @Test
    void testDeleteAccount_NotFound() {
        // Mock: account not found
        when(upiAccountRepository.findByUpiId("xyz@upi")).thenReturn(Optional.empty());

        // Call service delete
        upiAccountService.deleteAccount("xyz@upi");

        // Verify delete() never called
        verify(upiAccountRepository, never()).delete(any(UPIAccount.class));
    }

    /**
     * Scenario: Null UPI ID passed for delete.
     * Expected: IllegalArgumentException is thrown.
     */
    @Test
    void testDeleteAccount_NullId() {
        // Mock: repository throws exception for null input
        when(upiAccountRepository.findByUpiId(null)).thenThrow(new IllegalArgumentException("UPI ID cannot be null"));

        // Assert exception
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> upiAccountService.deleteAccount(null));

        assertEquals("UPI ID cannot be null", ex.getMessage());
    }
}
