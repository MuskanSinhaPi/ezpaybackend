package com.nwg.ezpay.repository;

import com.nwg.ezpay.entity.PaymentInstruction;
import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.InstructionStatus;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for PaymentInstruction entity.
 * Provides CRUD operations and lookup methods.
 *
 * Author: Muskan
 * Version: 1.3
 * Since: 2025-08-24
 * Revised: 2025-08-26 - added findByAccountHolderId and findByInstructionIdAndAccountHolder
 */
@Repository
public interface PaymentInstructionRepository extends JpaRepository<PaymentInstruction, Long> {

    /**
     * Find all payment instructions by account holder ID.
     *
     * @param accountHolderId Integer ID of the account holder
     * @return list of PaymentInstruction objects
     */
    @Query("SELECT p FROM PaymentInstruction p WHERE p.accountHolder.id = :accountHolderId")
    List<PaymentInstruction> findByAccountHolderId(Long accountHolderId);

    /**
     * Find a specific payment instruction by instruction ID and account holder.
     *
     * @param instructionId unique instruction ID
     * @param accountHolder the owner account holder
     * @return Optional containing PaymentInstruction if found
     */
    Optional<PaymentInstruction> findByIdAndAccountHolder(Long instructionId, AccountHolder accountHolder);

	/**
	 * Find payment instructions by status.
	 *
	 * @param status payment instruction status
	 * @return list of PaymentInstruction objects with the given status
	 */
	List<PaymentInstruction> findByStatus(InstructionStatus status);
	
	/**
	 * Find payment instructions by account holder and status.
	 *
	 * @param accountHolderId account holder ID
	 * @param status payment instruction status
	 * @return list of PaymentInstruction objects
	 */
	@Query("SELECT p FROM PaymentInstruction p WHERE p.accountHolder.id = :accountHolderId AND p.status = :status")
	List<PaymentInstruction> findByAccountHolderIdAndStatus(Long accountHolderId, InstructionStatus status);

}
