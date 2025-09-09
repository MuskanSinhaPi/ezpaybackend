package com.nwg.ezpay.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a banking account holder with personal and UPI details.
 * Author: Muskan
 * Version: 1.3
 * Since: 2025-08-25
 * Updated: 2025-09-02 - added balance field
 */
@Entity
@Table(name = "account_holders")
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_holder_id")
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(name = "upi_id", nullable = false, unique = true, length = 50)
    private String upiId;
    
    /** Current balance of the account holder */
    @Column(name = "balance", nullable = false)
    private Double balance = 0.0; //default
    
    /** List of beneficiaries owned by this account holder */
    @OneToMany(mappedBy = "accountHolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Beneficiary> beneficiaries;

    /**
     * Default constructor.
     */
    public AccountHolder() {
    }

    /**
     * Parameterized constructor to create an account holder.
     *
     * @param fullName     the full name of the account holder
     * @param email        the email address of the account holder
     * @param mobileNumber the 10-digit mobile number
     * @param upiId        the UPI ID linked to the account
     */
    public AccountHolder(String fullName, String username, String email, String mobileNumber, String upiId, Double balance) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.upiId = upiId;
        this.balance=balance;
    }

    /**
     * Gets the database-generated ID of the account holder.
     *
     * @return the account holder ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the full name of the account holder.
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the account holder.
     *
     * @param fullName new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    /**
     * Gets the login username of the account holder.
     *
     * @return username
     */
    public String getUsername() {
		return username;
	}
    
    /**
     * Sets the login username of the account holder.
     *
     * @param username
     */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
     * Gets the email of the account holder.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the account holder.
     *
     * @param email new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the mobile number of the account holder.
     *
     * @return mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the mobile number of the account holder.
     *
     * @param mobileNumber new mobile number
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * Gets the UPI ID of the account holder.
     *
     * @return UPI ID
     */
    public String getUpiId() {
        return upiId;
    }

    /**
     * Sets the UPI ID of the account holder.
     *
     * @param upiId new UPI ID
     */
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
    
    /**
     * Gets the current balance of the account holder.
     *
     * @return balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account holder.
     *
     * @param balance new balance
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
