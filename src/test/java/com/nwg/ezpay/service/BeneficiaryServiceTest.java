package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import com.nwg.ezpay.exception.ResourceNotFoundException;
import com.nwg.ezpay.repository.BeneficiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;

/**
 * Unit tests for {@link BeneficiaryService}, including edge-case and null-field handling
 * to achieve near-production-grade robustness.
 *
 * Author: Muskan
 * Version: 1.2
 * Since: 2025-09-05
 */
public class BeneficiaryServiceTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BeneficiaryService beneficiaryService;

    private AccountHolder holder;
    private Beneficiary beneficiary;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // constructor signature: (fullName, username, email, mobileNumber, upiId, balance)
        holder = new AccountHolder("Muskan", "muskan123", "muskan@example.com", "9876543210", "muskan@upi", 1000.0);
        beneficiary = new Beneficiary(holder, "John Doe", "123456789012","SBI", "SBIN0001234", "john@example.com", "9876543210");
        when(beneficiaryRepository.findByIdAndAccountHolder(anyLong(), any(AccountHolder.class)))
        .thenReturn(Optional.empty()); // for "no beneficiary" test

    }

    // ---------------------- Existing standard tests ---------------------- //

    @Test
    void testSaveBeneficiary() {
        when(beneficiaryRepository.save(beneficiary)).thenReturn(beneficiary);

        Beneficiary result = beneficiaryService.saveBeneficiary(beneficiary);

        assertEquals("John Doe", result.getName());
        verify(beneficiaryRepository, times(1)).save(beneficiary);
    }

    @Test
    void testGetAllBeneficiaries() {
        when(beneficiaryRepository.findAll()).thenReturn(Arrays.asList(beneficiary));

        List<Beneficiary> result = beneficiaryService.getAllBeneficiaries();

        assertEquals(1, result.size());
        verify(beneficiaryRepository, times(1)).findAll();
    }

    @Test
    void testGetBeneficiariesByAccountHolder() {
        when(beneficiaryRepository.findByAccountHolder(holder)).thenReturn(Arrays.asList(beneficiary));

        List<Beneficiary> result = beneficiaryService.getBeneficiariesByAccountHolder(holder);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetBeneficiaryById_Found() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));

        Beneficiary result = beneficiaryService.getBeneficiaryById(1L);

        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetBeneficiaryById_NotFound() {
        when(beneficiaryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> beneficiaryService.getBeneficiaryById(99L));
    }

    @Test
    void testGetBeneficiaryByIdAndAccountHolder_Found() {
        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(beneficiary));

        Beneficiary result = beneficiaryService.getBeneficiaryByIdAndAccountHolder(1L, holder);

        assertEquals("123456789012", result.getAccountNumber());
    }

    @Test
    void testGetBeneficiaryByIdAndAccountHolder_NotFound() {
        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> beneficiaryService.getBeneficiaryByIdAndAccountHolder(1L, holder));
    }

    @Test
    void testUpdateBeneficiary_Success() {
        Beneficiary updated = new Beneficiary(holder, "Jane Doe", "987654321098",
                "ICICI", "ICIC0004321", "jane@example.com", "9876501234");

        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(beneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class)))
                .thenReturn(updated);

        Beneficiary result = beneficiaryService.updateBeneficiary(1L, holder, updated);

        assertEquals("Jane Doe", result.getName());
        assertEquals("987654321098", result.getAccountNumber());
    }

    @Test
    void testUpdateBeneficiary_NotFound() {
        Beneficiary updated = new Beneficiary(holder, "Jane Doe", "987654321098",
                "ICICI", "ICIC0004321", "jane@example.com", "9876501234");

        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> beneficiaryService.updateBeneficiary(1L, holder, updated));
    }

    @Test
    void testDeleteBeneficiary_Success() {
        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(beneficiary));

        beneficiaryService.deleteBeneficiary(1L, holder);

        verify(beneficiaryRepository, times(1)).delete(beneficiary);
    }

    @Test
    void testDeleteBeneficiary_NotFound() {
        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> beneficiaryService.deleteBeneficiary(1L, holder));
    }

    // ---------------------- Edge-case + null-field tests ---------------------- //

    @Test
    void testSaveBeneficiary_NullOptionalFields() {
        beneficiary.setEmail(null);
        beneficiary.setPhone(null);

        when(beneficiaryRepository.save(beneficiary)).thenReturn(beneficiary);

        Beneficiary result = beneficiaryService.saveBeneficiary(beneficiary);

        assertNull(result.getEmail());
        assertNull(result.getPhone());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetBeneficiariesByAccountHolder_EmptyList() {
        when(beneficiaryRepository.findByAccountHolder(holder))
                .thenReturn(Collections.emptyList());

        assertTrue(beneficiaryService.getBeneficiariesByAccountHolder(holder).isEmpty());
    }

    @Test
    void testUpdateBeneficiary_WithNullFields() {
        Beneficiary updated = new Beneficiary();
        updated.setName(null);
        updated.setAccountNumber(null);

        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(beneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

        Beneficiary result = beneficiaryService.updateBeneficiary(1L, holder, updated);

        // original values retained
        assertEquals("John Doe", result.getName());
        assertEquals("123456789012", result.getAccountNumber());
    }

    @Test
    void testGetBeneficiaryById_Null() {
    	when(beneficiaryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

    	assertThrows(ResourceNotFoundException.class,
    	        () -> beneficiaryService.getBeneficiaryById(1L));
    }
}