package com.nwg.ezpay.repository;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Beneficiary entity.
 * Provides CRUD operations and lookup methods.
 *
 * Author: Muskan
 * Version: 1.2
 * Since: 2025-08-24
 * Revised: 2025-08-26 - added methods for account holder lookups and ownership validation
 */
@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    /**
     * Find all beneficiaries belonging to a specific account holder.
     *
     * @param accountHolder owner account holder
     * @return list of Beneficiary objects
     */
    List<Beneficiary> findByAccountHolder(AccountHolder accountHolder);

    /**
     * Find a specific beneficiary by ID and account holder.
     * Useful for validating ownership before updating or deleting.
     *
     * @param beneficiaryId unique beneficiary ID
     * @param accountHolder owner account holder
     * @return Optional containing Beneficiary if found
     */
    Optional<Beneficiary> findByIdAndAccountHolder(Long beneficiaryId, AccountHolder accountHolder);
    }
