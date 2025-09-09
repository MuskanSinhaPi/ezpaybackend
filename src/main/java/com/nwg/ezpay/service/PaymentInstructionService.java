package com.nwg.ezpay.service;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import com.nwg.ezpay.entity.InstructionStatus;
import com.nwg.ezpay.entity.PaymentInstruction;
import com.nwg.ezpay.exception.InsufficientBalanceException;
import com.nwg.ezpay.exception.ResourceNotFoundException;
import com.nwg.ezpay.repository.BeneficiaryRepository;
import com.nwg.ezpay.repository.PaymentInstructionRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service class to manage PaymentInstruction operations including
 * adding, fetching, updating, deleting instructions, and validating
 * ownership by account holder.
 *
 * Author: Muskan
 * Version: 1.5
 * Since:   2025-08-25
 * Revised: 2025-08-26 - added ownership validation using Optional
 * Revised: 2025-09-01 - added status-based query methods
 * Revised: 2025-09-05 - added mock bank transfer simulation
 */
@Service
public class PaymentInstructionService {

    private final PaymentInstructionRepository paymentInstructionRepository;
    private final AccountHolderService accountHolderService;
    private final BeneficiaryRepository beneficiaryRepository;

    /**
     * Constructor to inject PaymentInstructionRepository dependency.
     *
     * @param paymentInstructionRepository repository for PaymentInstruction
     */
    public PaymentInstructionService(PaymentInstructionRepository paymentInstructionRepository,
            AccountHolderService accountHolderService, BeneficiaryRepository beneficiaryRepository) {
        this.paymentInstructionRepository = paymentInstructionRepository;
        this.accountHolderService = accountHolderService;
		this.beneficiaryRepository = beneficiaryRepository;
    }

    /**
     * Fetch all payment instructions from the database.
     *
     * @return list of PaymentInstruction objects
     */
    public List<PaymentInstruction> getAllInstructions() {
        return paymentInstructionRepository.findAll();
    }

    /**
     * Fetch a payment instruction by its unique ID.
     *
     * @param id unique ID of the instruction
     * @return PaymentInstruction object
     * @throws ResourceNotFoundException if instruction not found
     */
    public PaymentInstruction getInstructionById(Long id) {
        return paymentInstructionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentInstruction not found with ID: " + id));
    }

    /**
     * Fetch all payment instructions associated with a specific account holder.
     *
     * @param accountHolderId Integer ID of the account holder
     * @return list of PaymentInstruction objects
     */
    public List<PaymentInstruction> getInstructionsByAccountHolder(Long accountHolderId) {
        return paymentInstructionRepository.findByAccountHolderId(accountHolderId);
    }

    /**
     * Fetch a specific payment instruction by instruction ID and account holder.
     * Useful for validating ownership before updating or deleting.
     *
     * @param instructionId unique instruction ID
     * @param accountHolder the owner account holder
     * @return PaymentInstruction object
     * @throws ResourceNotFoundException if instruction not found for this account holder
     */
    public PaymentInstruction getInstructionByIdAndAccountHolder(Long instructionId, AccountHolder accountHolder) {
        Optional<PaymentInstruction> optionalInstruction =
                paymentInstructionRepository.findByIdAndAccountHolder(instructionId, accountHolder);
        return optionalInstruction.orElseThrow(
                () -> new ResourceNotFoundException("PaymentInstruction ID " + instructionId +
                        " not found for AccountHolder ID: " + accountHolder.getId()));
    }

    /**
     * Add or update a payment instruction in the database.
     *
     * @param instruction PaymentInstruction object to save
     * @return saved PaymentInstruction object
     */
    public PaymentInstruction saveInstruction(PaymentInstruction instruction) {
    	    if (instruction.getId() == null) { // only for new instructions
    	        instruction.setStatus(InstructionStatus.DRAFT);
    	    }
    	    return paymentInstructionRepository.save(instruction);

    }

    /**
     * Delete a payment instruction by ID after validating ownership.
     *
     * @param instructionId unique ID of the instruction
     * @param accountHolder owner of the instruction
     * @throws ResourceNotFoundException if instruction not found for this account holder
     */
    public void deleteInstruction(Long instructionId, AccountHolder accountHolder) {
        PaymentInstruction existing = getInstructionByIdAndAccountHolder(instructionId, accountHolder);
        paymentInstructionRepository.delete(existing);
    }
    
    /**
     * Fetch all payment instructions filtered by status.
     *
     * @param status InstructionStatus to filter by
     * @return list of PaymentInstruction objects with given status
     */
    public List<PaymentInstruction> getInstructionsByStatus(InstructionStatus status) {
        return paymentInstructionRepository.findByStatus(status);
    }

    /**
     * Fetch all payment instructions for a specific account holder filtered by status.
     *
     * @param accountHolderId Integer ID of the account holder
     * @param status InstructionStatus to filter by
     * @return list of PaymentInstruction objects for this account holder and status
     */
    public List<PaymentInstruction> getInstructionsByAccountHolderAndStatus(Long accountHolderId,
                                                                            InstructionStatus status) {
        return paymentInstructionRepository.findByAccountHolderIdAndStatus(accountHolderId, status);
    }

    /**
     * Update the status of a payment instruction after validating ownership and balance.
     *
     * @param instructionId unique instruction ID
     * @param accountHolder owner of the instruction
     * @param newStatus new InstructionStatus to set
     * @return updated PaymentInstruction object
     * @throws ResourceNotFoundException if instruction not found for this account holder
     * @throws InsufficientBalanceException if account holder does not have enough balance
     */
    public PaymentInstruction updateInstructionStatus(Long instructionId,
                                                      AccountHolder accountHolder,
                                                      InstructionStatus newStatus) {
        PaymentInstruction existing = getInstructionByIdAndAccountHolder(instructionId, accountHolder);

        // Balance validation before submitting
        if (newStatus == InstructionStatus.SUBMITTED) {
            Double currentBalance = accountHolder.getBalance();
            if (currentBalance < existing.getAmount()) {
                throw new InsufficientBalanceException(
                        "AccountHolder ID " + accountHolder.getId() +
                        " has insufficient balance for instruction " + instructionId
                );
            }
            // Deduct balance (conceptual, real-world handled by banking APIs)
            accountHolderService.updateBalanceInternal(accountHolder, currentBalance - existing.getAmount());

        }

        existing.setStatus(newStatus);
        return paymentInstructionRepository.save(existing);
    }
    
    /**
     * Performs a simulated (mock) transfer for a given payment instruction.
     * <p>
     * This method evaluates the payment instruction against several conditions
     * to determine whether the transaction can succeed, fail, or remain pending.
     * The account holder's balance is updated only in case of successful transfers.
     * </p>
     *
     * <p>Possible outcomes:</p>
     * <ul>
     *   <li>{@link InstructionStatus#REJECTED} - if the beneficiary is missing,
     *       account balance is insufficient, or beneficiary details are invalid.</li>
     *   <li>{@link InstructionStatus#SUCCESS} - if all validations pass and
     *       the mock transfer succeeds (90% probability by simulation).</li>
     *   <li>{@link InstructionStatus#SUBMITTED} - if the transfer is conceptually
     *       still pending (simulated 10% failure scenario).</li>
     * </ul>
     *
     * @param instructionId the unique ID of the payment instruction to execute
     * @param accountHolder the owner of the account from which funds will be deducted
     * @return the updated {@link PaymentInstruction} object with status and remarks
     * @throws ResourceNotFoundException if the payment instruction does not exist
     */
    @Transactional
    public PaymentInstruction performMockTransfer(Long instructionId, AccountHolder accountHolder) {

        // Fetch the payment instruction and validate ownership
        PaymentInstruction instruction = getInstructionByIdAndAccountHolder(instructionId, accountHolder);
        Beneficiary beneficiary = instruction.getBeneficiary();

        // Condition 1: Missing beneficiary
        if (beneficiary == null) {
            instruction.setStatus(InstructionStatus.REJECTED);
            instruction.setRemarks("Beneficiary not found");
            return paymentInstructionRepository.save(instruction); // REJECTED due to missing recipient
        }

        // Condition 2: Insufficient account balance
        if (accountHolder.getBalance() < instruction.getAmount()) {
            instruction.setStatus(InstructionStatus.REJECTED);
            instruction.setRemarks("Insufficient funds");
            return paymentInstructionRepository.save(instruction); // REJECTED due to insufficient funds
        }

        // Condition 3: Invalid beneficiary details (e.g., missing account number or IFSC)
        if (beneficiary.getAccountNumber() == null || beneficiary.getIfsc() == null) {
            instruction.setStatus(InstructionStatus.REJECTED);
            instruction.setRemarks("Invalid beneficiary details");
            return paymentInstructionRepository.save(instruction); // REJECTED due to invalid recipient info
        }

        // Condition 4: Simulated transfer success/failure
        boolean success = Math.random() > 0.1; // 90% chance of success
        if (success) {
            // Deduct balance for successful transfer
            accountHolderService.updateBalanceInternal(accountHolder,
                    accountHolder.getBalance() - instruction.getAmount());

            instruction.setStatus(InstructionStatus.SUCCESS);
            instruction.setRemarks("Transaction successful"); // SUCCESSFUL transaction
        } else {
            // Simulate pending transfer
            instruction.setStatus(InstructionStatus.SUBMITTED);
            instruction.setRemarks("Transaction pending / processing by bank"); // Pending, still submitted
        }

        // Persist the updated instruction with status and remarks
        return paymentInstructionRepository.save(instruction);
    }
    
    /**
     * Creates a new payment instruction for a given sender (resolved by username)
     * and a beneficiary belonging to that sender.
     *
     * @param username       the username of the sender (account holder)
     * @param beneficiaryId  the ID of the beneficiary (must belong to sender)
     * @param amount         the transaction amount
     * @param remarks        optional transaction remarks
     * @return the newly created PaymentInstruction in DRAFT status
     */
    @Transactional
    public PaymentInstruction createInstruction(String username,
                                                Long beneficiaryId,
                                                Double amount,
                                                String remarks) {

        // 1. Resolve sender (AccountHolder) from username
        AccountHolder sender = accountHolderService.getByUsername(username);

        // 2. Resolve beneficiary — must belong to this sender
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndAccountHolder(beneficiaryId, sender)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Beneficiary ID " + beneficiaryId + " not found for username: " + username));


        // 3. Create instruction
        PaymentInstruction instruction = new PaymentInstruction();
        instruction.setAccountHolder(sender);        // ownership
        instruction.setBeneficiary(beneficiary);     // resolved beneficiary
        instruction.setAmount(amount);               // transfer amount
        instruction.setStatus(InstructionStatus.DRAFT); // default status
        instruction.setRemarks(remarks);

        // 4. Save to DB
        return paymentInstructionRepository.save(instruction);
    }
    
    private AccountHolder getCurrentAccountHolder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return accountHolderService.getByUsername(username);
    }


}
