package com.nwg.ezpay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity class representing a UPI (Unified Payments Interface) account.
 * <p>
 * This class maps to the {@code upi_accounts} table in the database
 * and stores essential information such as UPI ID, balance, security PIN,
 * and account status.
 * </p>
 *
 * Author : Vaishali Aggarwal
 */
@Entity
@Table(name = "upi_accounts")
public class UPIAccount {

    /** Unique identifier for the UPI account (primary key). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /** Unique UPI ID associated with the account (e.g., user@upi). */
    @Column(name = "UPI_ID", unique = true, nullable = false)
    private String upiId;

    /** Current balance in the account. Stored as integer for simplicity. */
    @Column(name = "BALANCE", nullable = false, precision = 15, scale = 2)
    private Integer balance;

    /** Security PIN used to authorize UPI transactions. */
    @Column(name = "PIN", nullable = false)
    private String pin;

    /** Flag indicating if the account is active (true) or deactivated (false). */
    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive = true;

    /** Timestamp when the account was created. Defaults to current time. */
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Default no-argument constructor required by JPA.
     */
    public UPIAccount() {
    }

    /**
     * Parameterized constructor to create a new UPI account.
     *
     * @param id        Unique identifier for the account
     * @param upiId     UPI ID associated with the account
     * @param balance   Initial balance of the account
     * @param isActive  Whether the account is active
     * @param createdAt Timestamp when the account was created
     * @param pin       Security PIN for authentication
     */
    public UPIAccount(Long id, String upiId, Integer balance, boolean isActive,
                      LocalDateTime createdAt, String pin) {
        this.id = id;
        this.upiId = upiId;
        this.balance = balance;
        this.pin = pin;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    /**
     * Gets the unique account ID.
     *
     * @return the account ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique account ID.
     *
     * @param id the account ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the UPI ID of the account.
     *
     * @return the UPI ID
     */
    public String getUpiId() {
        return upiId;
    }

    /**
     * Sets the UPI ID of the account.
     *
     * @param upiId the UPI ID to set
     */
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    /**
     * Gets the current balance in the account.
     *
     * @return the balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * Sets the balance for the account.
     *
     * @param balance the balance to set
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * Gets the security PIN for this account.
     *
     * @return the PIN
     */
    public String getPin() {
        return pin;
    }

    /**
     * Sets the security PIN for this account.
     *
     * @param pin the PIN to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Checks if the account is active.
     *
     * @return true if the account is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Updates the active status of the account.
     *
     * @param isActive the active status to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gets the creation timestamp of the account.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the account.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
