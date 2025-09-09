package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import com.nwg.ezpay.entity.InstructionStatus;
import com.nwg.ezpay.entity.PaymentInstruction;
import com.nwg.ezpay.exception.InsufficientBalanceException;
import com.nwg.ezpay.exception.ResourceNotFoundException;
import com.nwg.ezpay.repository.BeneficiaryRepository;
import com.nwg.ezpay.repository.PaymentInstructionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PaymentInstructionService}, including
 * complex edge-case simulations for transfers.
 *
 * Author: Muskan
 * Version: 1.1
 * Since: 2025-09-05
 */
public class PaymentInstructionServiceTest {

    @Mock
    private PaymentInstructionRepository paymentInstructionRepository;

    @Mock
    private AccountHolderService accountHolderService;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private PaymentInstructionService paymentInstructionService;

    private AccountHolder holder;
    private Beneficiary beneficiary;
    private PaymentInstruction instruction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        holder = new AccountHolder("Muskan", "muskan123", "muskan@example.com", "9876543210", "muskan@upi", 1000.0);
        beneficiary = new Beneficiary(holder, "John Doe", "123456789012","SBI", "SBIN0001234", "john@example.com", "9876543210");
        instruction = new PaymentInstruction(holder, beneficiary, 500.0, InstructionStatus.DRAFT, "Test transfer");
        when(paymentInstructionRepository.save(any(PaymentInstruction.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    }

    // ---------------------- Standard CRUD tests ---------------------- //

    @Test
    void testSaveInstruction() {
        PaymentInstruction result = paymentInstructionService.saveInstruction(instruction);

        assertEquals(500.0, result.getAmount());
        verify(paymentInstructionRepository, times(1)).save(instruction);
    }

    @Test
    void testGetAllInstructions() {
        when(paymentInstructionRepository.findAll()).thenReturn(Arrays.asList(instruction));

        List<PaymentInstruction> result = paymentInstructionService.getAllInstructions();

        assertEquals(1, result.size());
        verify(paymentInstructionRepository, times(1)).findAll();
    }

    @Test
    void testGetInstructionById_Found() {
        when(paymentInstructionRepository.findById(1L)).thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService.getInstructionById(1L);

        assertEquals(InstructionStatus.DRAFT, result.getStatus());
    }

    @Test
    void testGetInstructionById_NotFound() {
        when(paymentInstructionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentInstructionService.getInstructionById(99L));
    }

    @Test
    void testGetInstructionByIdAndAccountHolder_Found() {
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService.getInstructionByIdAndAccountHolder(1L, holder);

        assertEquals(500.0, result.getAmount());
    }

    @Test
    void testGetInstructionByIdAndAccountHolder_NotFound() {
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentInstructionService.getInstructionByIdAndAccountHolder(1L, holder));
    }

    @Test
    void testDeleteInstruction_Success() {
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        paymentInstructionService.deleteInstruction(1L, holder);

        verify(paymentInstructionRepository, times(1)).delete(instruction);
    }

    @Test
    void testDeleteInstruction_NotFound() {
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentInstructionService.deleteInstruction(1L, holder));
    }

    @Test
    void testUpdateInstructionStatus_SufficientBalance() {
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService
                .updateInstructionStatus(1L, holder, InstructionStatus.SUBMITTED);

        assertEquals(InstructionStatus.SUBMITTED, result.getStatus());
        verify(accountHolderService, times(1))
                .updateBalanceInternal(eq(holder), any(Double.class));
    }

    @Test
    void testUpdateInstructionStatus_InsufficientBalance() {
        holder.setBalance(100.0); // less than instruction amount

        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        assertThrows(InsufficientBalanceException.class,
                () -> paymentInstructionService.updateInstructionStatus(1L, holder, InstructionStatus.SUBMITTED));

        verify(accountHolderService, never()).updateBalanceInternal(any(), any());
    }

    // ---------------------- performMockTransfer tests ---------------------- //

    @Test
    void testPerformMockTransfer_Success() {
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService.performMockTransfer(1L, holder);

        assertTrue(result.getStatus() == InstructionStatus.SUCCESS
                || result.getStatus() == InstructionStatus.SUBMITTED);
        verify(paymentInstructionRepository, atLeastOnce()).save(instruction);
    }

    @Test
    void testPerformMockTransfer_RejectedNoBeneficiary() {
        instruction.setBeneficiary(null);

        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService.performMockTransfer(1L, holder);

        assertEquals(InstructionStatus.REJECTED, result.getStatus());
        assertEquals("Beneficiary not found", result.getRemarks());
    }

    @Test
    void testPerformMockTransfer_RejectedInsufficientFunds() {
        holder.setBalance(100.0);
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService.performMockTransfer(1L, holder);

        assertEquals(InstructionStatus.REJECTED, result.getStatus());
        assertEquals("Insufficient funds", result.getRemarks());
    }

    @Test
    void testPerformMockTransfer_RejectedInvalidBeneficiary() {
        beneficiary.setAccountNumber(null);
        instruction.setBeneficiary(beneficiary);
        when(paymentInstructionRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(instruction));

        PaymentInstruction result = paymentInstructionService.performMockTransfer(1L, holder);

        assertEquals(InstructionStatus.REJECTED, result.getStatus());
        assertEquals("Invalid beneficiary details", result.getRemarks());
    }

    // ---------------------- createInstruction tests ---------------------- //

    @Test
    void testCreateInstruction_Success() {
        when(accountHolderService.getByUsername("Muskan")).thenReturn(holder);
        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.of(beneficiary));

        PaymentInstruction result = paymentInstructionService.createInstruction(
                "Muskan", 1L, 500.0, "Test remarks"
        );

        assertEquals(InstructionStatus.DRAFT, result.getStatus());
        assertEquals("Test remarks", result.getRemarks());
        assertEquals(holder, result.getAccountHolder());
    }

    @Test
    void testCreateInstruction_BeneficiaryNotFound() {
        when(accountHolderService.getByUsername("Muskan")).thenReturn(holder);
        when(beneficiaryRepository.findByIdAndAccountHolder(1L, holder))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentInstructionService.createInstruction("Muskan", 1L, 500.0, "Test"));
    }

    // ---------------------- Edge-case + null-field tests ---------------------- //

    @Test
    void testSaveInstruction_NullRemarks() {
        instruction.setRemarks(null);

        PaymentInstruction result = paymentInstructionService.saveInstruction(instruction);

        assertNull(result.getRemarks());
        assertEquals(InstructionStatus.DRAFT, result.getStatus());
    }

    @Test
    void testGetInstructionsByAccountHolder_EmptyList() {
        when(paymentInstructionRepository.findByAccountHolderId(holder.getId()))
                .thenReturn(Arrays.asList());

        List<PaymentInstruction> result = paymentInstructionService.getInstructionsByAccountHolder(holder.getId());

        assertTrue(result.isEmpty());
    }
}
