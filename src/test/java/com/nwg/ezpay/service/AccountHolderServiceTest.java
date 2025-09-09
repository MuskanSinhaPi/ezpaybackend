package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.exception.ResourceNotFoundException;
import com.nwg.ezpay.repository.AccountHolderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AccountHolderService} using Mockito and JUnit 5.
 * Fully updated to match the AccountHolder entity constructor and include realistic scenarios.
 *
 * Author: Muskan
 * Version: 1.1
 * Since: 2025-09-05
 */
public class AccountHolderServiceTest {

    @Mock
    private AccountHolderRepository accountHolderRepository;

    @InjectMocks
    private AccountHolderService accountHolderService;

    private AccountHolder holder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Corrected constructor: fullName, username, email, mobileNumber, upiId, balance
        holder = new AccountHolder("Muskan", "muskan123", "muskan@example.com", "9876543210", "muskan@upi", 1000.0);
    }

    /**
     * Test fetching all account holders.
     */
    @Test
    void testGetAllAccountHolders() {
        when(accountHolderRepository.findAll()).thenReturn(Arrays.asList(holder));

        List<AccountHolder> result = accountHolderService.getAllAccountHolders();

        assertEquals(1, result.size());
        verify(accountHolderRepository, times(1)).findAll();
    }

    /**
     * Test fetching an account holder by ID when found.
     */
    @Test
    void testGetAccountHolderById_Found() {
        when(accountHolderRepository.findById(1L)).thenReturn(Optional.of(holder));

        AccountHolder result = accountHolderService.getAccountHolderById(1L);

        assertEquals("Muskan", result.getFullName());
        assertEquals("muskan123", result.getUsername());
        verify(accountHolderRepository, times(1)).findById(1L);
    }

    /**
     * Test fetching an account holder by ID when not found.
     */
    @Test
    void testGetAccountHolderById_NotFound() {
        when(accountHolderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountHolderService.getAccountHolderById(99L));
    }

    /**
     * Test adding an account holder.
     */
    @Test
    void testAddAccountHolder() {
        when(accountHolderRepository.save(holder)).thenReturn(holder);

        AccountHolder result = accountHolderService.addAccountHolder(holder);

        assertEquals("Muskan", result.getFullName());
        assertEquals("muskan123", result.getUsername());
        verify(accountHolderRepository, times(1)).save(holder);
    }

    /**
     * Test fetching an account holder by username when found.
     */
    @Test
    void testGetByUsername_Found() {
        when(accountHolderRepository.findByUsername("muskan123")).thenReturn(Optional.of(holder));

        AccountHolder result = accountHolderService.getByUsername("muskan123");

        assertEquals("Muskan", result.getFullName());
        assertEquals("muskan123", result.getUsername());
        verify(accountHolderRepository, times(1)).findByUsername("muskan123");
    }

    /**
     * Test fetching an account holder by username when not found.
     */
    @Test
    void testGetByUsername_NotFound() {
        when(accountHolderRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountHolderService.getByUsername("unknown"));
    }

    /**
     * Test updating email of an account holder.
     */
    @Test
    void testUpdateEmail() {
        when(accountHolderRepository.findById(1L)).thenReturn(Optional.of(holder));
        when(accountHolderRepository.save(holder)).thenReturn(holder);

        AccountHolder result = accountHolderService.updateEmail(1L, "newmail@example.com");

        assertEquals("newmail@example.com", result.getEmail());
        verify(accountHolderRepository, times(1)).save(holder);
    }

    /**
     * Test updating mobile number of an account holder.
     */
    @Test
    void testUpdateMobileNumber() {
        when(accountHolderRepository.findById(1L)).thenReturn(Optional.of(holder));
        when(accountHolderRepository.save(holder)).thenReturn(holder);

        AccountHolder result = accountHolderService.updateMobileNumber(1L, "9123456789");

        assertEquals("9123456789", result.getMobileNumber());
        verify(accountHolderRepository, times(1)).save(holder);
    }

    /**
     * Test updating balance internally (package-private method).
     */
    @Test
    void testUpdateBalanceInternal() {
        when(accountHolderRepository.save(holder)).thenReturn(holder);

        AccountHolder result = accountHolderService.updateBalanceInternal(holder, 2000.0);

        assertEquals(2000.0, result.getBalance());
        verify(accountHolderRepository, times(1)).save(holder);
    }

    /**
     * Test deleting an account holder.
     */
    @Test
    void testDeleteAccountHolder() {
        when(accountHolderRepository.findById(1L)).thenReturn(Optional.of(holder));

        accountHolderService.deleteAccountHolder(1L);

        verify(accountHolderRepository, times(1)).delete(holder);
    }
}