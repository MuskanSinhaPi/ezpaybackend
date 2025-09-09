package com.nwg.ezpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nwg.ezpay.entity.UPIAccount;
import java.util.Optional;

/**
 * Repository interface for UPI Account entities.
 * Provides CRUD operations and custom finder methods for UPI accounts.
 * Extends JpaRepository to inherit common data access operations.
 * 
 * Author : Vaishali Aggarwal
 */
@Repository
public interface UPIAccountRepository extends JpaRepository<UPIAccount, Long> {

	/**
	 * Finds a UPI account by its UPI ID.
	 * 
	 * @param upiId the UPI ID to search for
	 * @return Optional containing the UPI account if found, empty otherwise
	 */
	Optional<UPIAccount> findByUpiId(String upiId);
}
