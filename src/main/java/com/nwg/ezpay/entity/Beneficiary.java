package com.nwg.ezpay.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a beneficiary linked to an account holder.
 * Each account holder can have multiple beneficiaries.
 * Author: Muskan
 * Version: 1.4
 * Since: 2025-08-25
 * Updated: 2025-08-26
 */
@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    /** Primary key of the beneficiary table */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficiary_id")
    private Long id;

    /** The account holder who owns this beneficiary */
    @ManyToOne
    @JoinColumn(name = "account_holder_id", nullable = false)
    @Schema(hidden = true)
    private AccountHolder accountHolder;

    /** Name of the beneficiary */
    @Column(name = "beneficiary_name", nullable = false, length = 100)
    private String name;

    /** Bank account number of the beneficiary */
    @Column(name = "beneficiary_account_number", nullable = false, length = 20)
    private String accountNumber;
    
    /** Bank name of beneficiary (e.g., SBI, ICICI). */
    @Column(nullable = false)
    private String bankName;
    
    /** IFSC code of the beneficiary's bank */
    @Column(name = "ifsc_code", nullable = false, length = 11)
    private String ifsc;

	/** Email Id of the beneficiary */
    @Column(name = "email", length = 150)
    private String email;

    /** Mobile number of the beneficiary */
    @Column(name = "phone", length = 10)
    private String phone;

    /**
     * Default constructor required by JPA
     */
    public Beneficiary() {
    }

    /**
     * Parameterized constructor to create a beneficiary.
     *
     * @param accountHolder the linked account holder
     * @param name          name of the beneficiary
     * @param accountNumber bank account number of the beneficiary
     */
    public Beneficiary(AccountHolder accountHolder, String name, String accountNumber, String bankName, String ifsc, String email, String phone) {
        this.accountHolder = accountHolder;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.ifsc = ifsc;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Gets the beneficiary ID.
     *
     * @return unique beneficiary ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the linked account holder.
     *
     * @return account holder
     */
    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    /**
     * Sets the linked account holder.
     *
     * @param accountHolder account holder to link
     */
    public void setAccountHolder(AccountHolder accountHolder) {
        this.accountHolder = accountHolder;
    }

    /**
     * Gets the beneficiary's name.
     *
     * @return beneficiary name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the beneficiary's name.
     *
     * @param name new beneficiary name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the beneficiary's bank account number.
     *
     * @return account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the beneficiary's bank account number.
     *
     * @param accountNumber new account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    /**
     * Gets the beneficiary's bank name
     *
     * @return bankName beneficiary's bank name
     */
    public String getBankName() {
		return bankName;
	}
    
    /**
     * Sets the beneficiary's bank name
     *
     * @param bankName beneficiary's bank name
     */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
     * Gets the IFSC code of the beneficiary's bank branch.
     *
     * @return beneficiary's bank IFSC code
     */
    public String getIfsc() {
 		return ifsc;
 	}

    /**
     * Sets the beneficiary's bank branch IFSC code.
     *
     * @param ifsc new bank IFSC code
     */
 	public void setIfsc(String ifsc) {
 		this.ifsc = ifsc;
 	}

    /**
     * Gets the beneficiary's email id.
     *
     * @return beneficiary's email id
     */
 	public String getEmail() {
 		return email;
 	}

    /**
     * Sets the beneficiary's email id.
     *
     * @param email new email id
     */
 	public void setEmail(String email) {
 		this.email = email;
 	}

    /**
     * Gets the beneficiary's mobile number
     *
     * @return phone beneficiary's mobile number
     */
 	public String getPhone() {
 		return phone;
 	}
 	
    /**
     * Sets the beneficiary's mobile number.
     *
     * @param phone new mobile number
     */
 	public void setPhone(String phone) {
 		this.phone = phone;
 	}

}
