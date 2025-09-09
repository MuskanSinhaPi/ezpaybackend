package com.nwg.ezpay.repository;

import com.nwg.ezpay.entity.AccountHolder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for AccountHolder entity.
 * Provides CRUD operations and lookup methods.
 *
 * Author: Muskan
 * Version: 1.0
 * Since: 2025-08-24
 * Revised: 2025-08-24 - added findByEmail, findByMobileNumber, findByUpiId
 */
@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

    /**
     * Find an account holder by email.
     *
     * @param email unique email of account holder
     * @return Optional containing AccountHolder if found
     */
    Optional<AccountHolder> findByEmail(String email);

    /**
     * Find an account holder by mobile number.
     *
     * @param mobileNumber unique mobile number
     * @return Optional containing AccountHolder if found
     */
    Optional<AccountHolder> findByMobileNumber(String mobileNumber);

    /**
     * Find an account holder by UPI ID.
     *
     * @param upiId unique UPI identifier
     * @return Optional containing AccountHolder if found
     */
    Optional<AccountHolder> findByUpiId(String upiId);
  
    /**
     * Find an account holder by  username.
     *
     * @param username unique username of account holder
     * @return Optional containing AccountHolder if found
     */
    Optional<AccountHolder> findByUsername(String username);
}
