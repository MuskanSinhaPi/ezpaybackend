package com.nwg.ezpay.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nwg.ezpay.entity.UPIAccount;
import com.nwg.ezpay.entity.UPITransaction;
import com.nwg.ezpay.exception.AccountNotFoundException;
import com.nwg.ezpay.exception.InsufficientBalanceException;
import com.nwg.ezpay.exception.InvalidAmountException;
import com.nwg.ezpay.exception.InvalidPinException;
import com.nwg.ezpay.exception.TransactionNotFoundException;
import com.nwg.ezpay.repository.UPIAccountRepository;
import com.nwg.ezpay.repository.UPITransactionRepository;

/**
 * Service class for managing UPI transactions.
 * <p>
 * This service handles business logic for creating, verifying, processing,
 * retrieving, and deleting UPI transactions. It also validates accounts,
 * balances, and PINs before processing a transaction.
 * </p>
 *
 * Author : Vaishali Aggarwal
 */
@Service
public class UPITransactionService {

    private final UPITransactionRepository upiTransactionRepository;
    private final UPIAccountRepository upiAccountRepository;

    /**
     * Constructs the {@link UPITransactionService} with required repositories.
     *
     * @param upiTransactionRepository Repository for UPI transactions
     * @param upiAccountRepository     Repository for UPI accounts
     */
    public UPITransactionService(UPITransactionRepository upiTransactionRepository,
                                 UPIAccountRepository upiAccountRepository) {
        this.upiTransactionRepository = upiTransactionRepository;
        this.upiAccountRepository = upiAccountRepository;
    }

    /**
     * Creates a new UPI transaction after validating sender, receiver, UPI ID format,
     * and available balance.
     *
     * @param upiTransaction The transaction object containing sender, receiver,
     *                       and amount details
     * @return The saved {@link UPITransaction} with status set to "PENDING"
     * @throws IllegalArgumentException       if the receiver UPI ID format is invalid
     * @throws InvalidAmountException         if the amount is null or less than or equal to zero
     * @throws AccountNotFoundException       if sender or receiver account does not exist
     * @throws InsufficientBalanceException   if sender has insufficient funds
     */
    public UPITransaction addUPITransaction(UPITransaction upiTransaction) {
        if (upiTransaction.getReceiverUpiId() == null ||
                !upiTransaction.getReceiverUpiId().matches("^[a-zA-Z0-9._-]+@[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid UPI ID format.");
        }

        if (upiTransaction.getAmount() == null || upiTransaction.getAmount() <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }

        UPIAccount sender = upiAccountRepository.findByUpiId(upiTransaction.getSenderUpiId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Sender account not found for UPI ID: " + upiTransaction.getSenderUpiId()
                ));
        UPIAccount receiver = upiAccountRepository.findByUpiId(upiTransaction.getReceiverUpiId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Reciever account not found for UPI ID: " + upiTransaction.getReceiverUpiId()
                ));

        if (upiTransaction.getAmount().compareTo(sender.getBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance in sender's account.");
        }

        upiTransaction.setStatus("PENDING");
        upiTransaction.setTimestamp(LocalDateTime.now());

        return upiTransactionRepository.save(upiTransaction);
    }

    /**
     * Verifies a UPI transaction by checking the sender's PIN and processes the transfer
     * of funds if valid.
     *
     * @param transactionId The ID of the transaction to verify
     * @param pin           The UPI PIN of the sender
     * @return The updated {@link UPITransaction} with status set to "SUCCESS"
     * @throws TransactionNotFoundException   if the transaction does not exist
     * @throws IllegalStateException          if the transaction is already processed
     * @throws AccountNotFoundException       if sender or receiver account does not exist
     * @throws InvalidPinException            if the entered PIN is incorrect
     * @throws InsufficientBalanceException   if sender has insufficient funds
     */
    public UPITransaction verifyTransactionPin(Integer transactionId, String pin) {
        UPITransaction transaction = upiTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        if (!"PENDING".equals(transaction.getStatus())) {
            throw new IllegalStateException("Transaction already processed.");
        }

        UPIAccount sender = upiAccountRepository.findByUpiId(transaction.getSenderUpiId())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        UPIAccount receiver = upiAccountRepository.findByUpiId(transaction.getReceiverUpiId())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if (!sender.getPin().equals(pin)) {
            throw new InvalidPinException("Invalid PIN provided");
        }

        if (sender.getBalance() < transaction.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in sender's account");
        }

        // Deduct money
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount());
        upiAccountRepository.save(sender);

        // Mark transaction as successful
        transaction.setStatus("SUCCESS");
        return upiTransactionRepository.save(transaction);
    }

    /**
     * Retrieves all UPI transactions stored in the system.
     *
     * @return A list of {@link UPITransaction}
     */
    public List<UPITransaction> showAllUPITransactions() {
        return upiTransactionRepository.findAll();
    }

    /**
     * Deletes a UPI transaction by its ID.
     *
     * @param id The transaction ID
     * @return true if the transaction was deleted, false if not found
     */
    public boolean deleteUPITransaction(int id) {
        if (upiTransactionRepository.existsById(id)) {
            upiTransactionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a specific UPI transaction by its ID.
     *
     * @param id The transaction ID
     * @return An {@link Optional} containing the transaction if found, otherwise empty
     */
    public Optional<UPITransaction> getUPITransactionById(int id) {
        return upiTransactionRepository.findById(id);
    }

    /**
     * Retrieves all UPI transactions with a specific status.
     *
     * @param status The transaction status 
     * @return A list of {@link UPITransaction} matching the status
     */
    public List<UPITransaction> getUPITransactionsByStatus(String status) {
        return upiTransactionRepository.findByStatus(status);
    }

    /**
     * Retrieves all UPI transactions where the given UPI ID is the receiver.
     *
     * @param upiId The receiver's UPI ID
     * @return A list of {@link UPITransaction} for the given UPI ID
     */
    public List<UPITransaction> getUPITransactionsByUpiId(String upiId) {
        return upiTransactionRepository.findByReceiverUpiId(upiId);
    }
}
