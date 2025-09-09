package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.UPIAccount;
import com.nwg.ezpay.entity.UPITransaction;
import com.nwg.ezpay.exception.AccountNotFoundException;
import com.nwg.ezpay.exception.InsufficientBalanceException;
import com.nwg.ezpay.exception.InvalidAmountException;
import com.nwg.ezpay.exception.InvalidPinException;
import com.nwg.ezpay.exception.TransactionNotFoundException;
import com.nwg.ezpay.repository.UPIAccountRepository;
import com.nwg.ezpay.repository.UPITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
 * Unit tests for {@link UPITransactionService}.
 *
 * This test class verifies the following functionalities of UPITransactionService:
 * - Adding a new UPI transaction (valid and invalid cases)
 * - Verifying a transaction using PIN (valid, wrong PIN, insufficient balance, etc.)
 * - Fetching all transactions or specific ones by ID/status
 * - Deleting transactions
 *
 * Mockito is used to mock repository dependencies so that:
 * - No actual database calls are made
 * - Behavior is simulated and tested in isolation
 *
 * Author: Aditi Roy 
 */
class UPITransactionServiceTest {

    // Mocked repository for transaction persistence
    @Mock
    private UPITransactionRepository upiTransactionRepository;

    // Mocked repository for account lookup and updates
    @Mock
    private UPIAccountRepository upiAccountRepository;

    // Service under test (injected with mocks)
    @InjectMocks
    private UPITransactionService upiTransactionService;

    // Test objects reused across tests
    private UPIAccount senderAccount;
    private UPIAccount receiverAccount;
    private UPITransaction testTransaction;

    /**
     * Setup method runs before each test.
     * - Initializes Mockito mocks
     * - Creates sender and receiver UPI accounts
     * - Creates a sample UPI transaction (initially PENDING)
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        senderAccount = new UPIAccount();
        senderAccount.setId(1L);
        senderAccount.setUpiId("john@upi");
        senderAccount.setBalance(5000);
        senderAccount.setPin("1234");

        receiverAccount = new UPIAccount();
        receiverAccount.setId(2L);
        receiverAccount.setUpiId("receiver@upi");
        receiverAccount.setBalance(2000);
        receiverAccount.setPin("5678");

        testTransaction = new UPITransaction();
        testTransaction.setSenderUpiId("john@upi");
        testTransaction.setReceiverUpiId("receiver@upi");
        testTransaction.setAmount(1000);
        testTransaction.setStatus("PENDING");
        testTransaction.setTimestamp(LocalDateTime.now());
    }

    // =============================================================
    // ADD TRANSACTION TESTS
    // =============================================================

    /**
     * Scenario: Adding a valid transaction.
     * Expectation: Transaction is saved successfully with status "PENDING".
     */
    @Test
    void testAddTransaction_Success() {
        // Mock both accounts exist
        when(upiAccountRepository.findByUpiId("john@upi")).thenReturn(Optional.of(senderAccount));
        when(upiAccountRepository.findByUpiId("receiver@upi")).thenReturn(Optional.of(receiverAccount));
        // Mock transaction save
        when(upiTransactionRepository.save(any(UPITransaction.class))).thenReturn(testTransaction);

        UPITransaction saved = upiTransactionService.addUPITransaction(testTransaction);

        assertNotNull(saved);
        assertEquals("PENDING", saved.getStatus());
        verify(upiTransactionRepository, times(1)).save(testTransaction);
    }

    /**
     * Scenario: Receiver UPI ID is invalid format.
     * Expectation: IllegalArgumentException is thrown.
     */
    @Test
    void testAddTransaction_InvalidUpiId() {
        testTransaction.setReceiverUpiId("invalidupi"); // invalid format

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> upiTransactionService.addUPITransaction(testTransaction));

        assertEquals("Invalid UPI ID format.", ex.getMessage());
    }

    /**
     * Scenario: Amount <= 0.
     * Expectation: InvalidAmountException is thrown.
     */
    @Test
    void testAddTransaction_InvalidAmount() {
        testTransaction.setAmount(0); // invalid

        InvalidAmountException ex = assertThrows(InvalidAmountException.class,
                () -> upiTransactionService.addUPITransaction(testTransaction));

        assertEquals("Amount must be greater than zero.", ex.getMessage());
    }

    /**
     * Scenario: Sender account does not exist in repository.
     * Expectation: AccountNotFoundException is thrown.
     */
    @Test
    void testAddTransaction_AccountNotFound() {
        when(upiAccountRepository.findByUpiId("john@upi")).thenReturn(Optional.empty());

        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class,
                () -> upiTransactionService.addUPITransaction(testTransaction));

        assertTrue(ex.getMessage().contains("Sender account not found"));
    }

    /**
     * Scenario: Sender has insufficient balance.
     * Expectation: InsufficientBalanceException is thrown.
     */
    @Test
    void testAddTransaction_InsufficientBalance() {
        senderAccount.setBalance(500); // less than amount
        when(upiAccountRepository.findByUpiId("john@upi")).thenReturn(Optional.of(senderAccount));
        when(upiAccountRepository.findByUpiId("receiver@upi")).thenReturn(Optional.of(receiverAccount));

        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class,
                () -> upiTransactionService.addUPITransaction(testTransaction));

        assertEquals("Insufficient balance in sender's account.", ex.getMessage());
    }

    // // =============================================================
    // // VERIFY TRANSACTION PIN TESTS
    // // =============================================================

    // /**
    //  * Scenario: Correct PIN provided, sufficient balance.
    //  * Expectation: Transaction succeeds, balances updated.
    //  */
    // @Test
    // void testVerifyTransactionPin_Success() {
    //     when(upiTransactionRepository.findById(101)).thenReturn(Optional.of(testTransaction));
    //     when(upiAccountRepository.findByUpiId("john@upi")).thenReturn(Optional.of(senderAccount));
    //     when(upiAccountRepository.findByUpiId("receiver@upi")).thenReturn(Optional.of(receiverAccount));
    //     when(upiTransactionRepository.save(any(UPITransaction.class))).thenReturn(testTransaction);

    //     UPITransaction result = upiTransactionService.verifyTransactionPin(1, "1234");

    //     assertEquals("SUCCESS", result.getStatus());
    //     assertEquals(4000, senderAccount.getBalance()); // 5000 - 1000
    //     assertEquals(3000, receiverAccount.getBalance()); // 2000 + 1000
    // }

    /**
     * Scenario: Wrong PIN entered.
     * Expectation: InvalidPinException is thrown.
     */
    @Test
    void testVerifyTransactionPin_WrongPin() {
        when(upiTransactionRepository.findById(101)).thenReturn(Optional.of(testTransaction));
        when(upiAccountRepository.findByUpiId("john@upi")).thenReturn(Optional.of(senderAccount));
        when(upiAccountRepository.findByUpiId("receiver@upi")).thenReturn(Optional.of(receiverAccount));

        InvalidPinException ex = assertThrows(InvalidPinException.class,
                () -> upiTransactionService.verifyTransactionPin(101, "9999"));

        assertEquals("Invalid PIN provided", ex.getMessage());
    }

    /**
     * Scenario: Transaction ID not found.
     * Expectation: TransactionNotFoundException is thrown.
     */
    @Test
    void testVerifyTransactionPin_TransactionNotFound() {
        when(upiTransactionRepository.findById(999)).thenReturn(Optional.empty());

        TransactionNotFoundException ex = assertThrows(TransactionNotFoundException.class,
                () -> upiTransactionService.verifyTransactionPin(999, "1234"));

        assertEquals("Transaction not found", ex.getMessage());
    }

    /**
     * Scenario: Transaction amount exceeds sender's balance.
     * Expectation: InsufficientBalanceException is thrown.
     */
    @Test
    void testVerifyTransactionPin_InsufficientBalance() {
        testTransaction.setAmount(6000); // more than balance
        when(upiTransactionRepository.findById(101)).thenReturn(Optional.of(testTransaction));
        when(upiAccountRepository.findByUpiId("john@upi")).thenReturn(Optional.of(senderAccount));
        when(upiAccountRepository.findByUpiId("receiver@upi")).thenReturn(Optional.of(receiverAccount));

        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class,
                () -> upiTransactionService.verifyTransactionPin(101, "1234"));

        assertEquals("Insufficient balance in sender's account", ex.getMessage());
    }

    // =============================================================
    // FETCH ALL TRANSACTIONS TESTS
    // =============================================================

    /**
     * Scenario: Repository returns a list with 1 transaction.
     * Expectation: Service returns same list.
     */
    @Test
    void testShowAllTransactions() {
        when(upiTransactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction));

        List<UPITransaction> result = upiTransactionService.showAllUPITransactions();

        assertEquals(1, result.size());
        verify(upiTransactionRepository, times(1)).findAll();
    }

    /**
     * Scenario: Repository returns no transactions.
     * Expectation: Service returns empty list.
     */
    @Test
    void testShowAllTransactions_Empty() {
        when(upiTransactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<UPITransaction> result = upiTransactionService.showAllUPITransactions();

        assertTrue(result.isEmpty());
    }

    // =============================================================
    // DELETE TRANSACTION TESTS
    // =============================================================

    /**
     * Scenario: Transaction exists.
     * Expectation: Transaction deleted successfully.
     */
    @Test
    void testDeleteTransaction_Found() {
        when(upiTransactionRepository.existsById(101)).thenReturn(true);

        boolean result = upiTransactionService.deleteUPITransaction(101);

        assertTrue(result);
        verify(upiTransactionRepository, times(1)).deleteById(101);
    }

    /**
     * Scenario: Transaction does not exist.
     * Expectation: Delete method not called, false returned.
     */
    @Test
    void testDeleteTransaction_NotFound() {
        when(upiTransactionRepository.existsById(999)).thenReturn(false);

        boolean result = upiTransactionService.deleteUPITransaction(999);

        assertFalse(result);
        verify(upiTransactionRepository, never()).deleteById(999);
    }

    // =============================================================
    // FIND BY ID TESTS
    // =============================================================

    /**
     * Scenario: Transaction with given ID exists.
     * Expectation: Optional contains transaction.
     */
    @Test
    void testGetTransactionById_Found() {
        when(upiTransactionRepository.findById(101)).thenReturn(Optional.of(testTransaction));

        Optional<UPITransaction> result = upiTransactionService.getUPITransactionById(101);

        assertTrue(result.isPresent());
    }

    /**
     * Scenario: Transaction not found by ID.
     * Expectation: Optional is empty.
     */
    @Test
    void testGetTransactionById_NotFound() {
        when(upiTransactionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<UPITransaction> result = upiTransactionService.getUPITransactionById(999);

        assertFalse(result.isPresent());
    }

    // =============================================================
    // FIND BY STATUS TESTS
    // =============================================================

    /**
     * Scenario: Transactions with given status exist.
     * Expectation: Service returns list filtered by status.
     */
    @Test
    void testGetTransactionsByStatus() {
        when(upiTransactionRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(testTransaction));

        List<UPITransaction> result = upiTransactionService.getUPITransactionsByStatus("PENDING");

        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }
}
