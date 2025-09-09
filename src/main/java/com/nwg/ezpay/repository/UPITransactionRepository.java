package com.nwg.ezpay.repository;

import org.springframework.stereotype.Repository;
import com.nwg.ezpay.entity.UPITransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for UPI Transaction entities.
 * Provides CRUD operations and custom finder methods for UPI transactions.
 * Extends JpaRepository to inherit common data access operations.
 * 
 * Author : Vaishali Aggarwal
 */
@Repository
public interface UPITransactionRepository extends JpaRepository<UPITransaction, Integer> {

    /**
     * Finds all transactions with a specific status.
     * 
     * @param status the transaction status to search for
     * @return List of transactions matching the given status
     */
    List<UPITransaction> findByStatus(String status);

    /**
     * Finds a transaction by its ID.
     * This method overrides the default findById from JpaRepository
     * to provide specific documentation.
     * 
     * @param id the transaction ID to search for
     * @return Optional containing the transaction if found, empty otherwise
     */
    Optional<UPITransaction> findById(Integer id);
    
    List<UPITransaction> findByReceiverUpiId(String receiverUpiId);
}
