package com.nwg.ezpay.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a payment instruction from an account holder to a beneficiary.
 * Author: Muskan
 * Version: 1.4
 * Since: 2025-08-25
 * Updated: 2025-08-26
 */
@Entity
@Table(name = "payment_instructions")
public class PaymentInstruction {

    /** Primary key of payment instruction table */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instruction_id")
    private Long id;

    /** Account holder initiating the payment */
    @ManyToOne
    @JoinColumn(name = "account_holder_id", nullable = false)
    @Schema(hidden = true)
    private AccountHolder accountHolder;

    /** Beneficiary receiving the payment */
    @ManyToOne
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @Schema(hidden = true)
    private Beneficiary beneficiary;

    /** Amount to be transferred */
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Column(name = "remarks", length = 250)
    private String remarks;

    /** Current status of the payment instruction */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InstructionStatus status = InstructionStatus.DRAFT;

    /** Default constructor required by JPA */
    public PaymentInstruction() {
    }

    /**
     * Parameterized constructor to create a payment instruction.
     *
     * @param accountHolder the account holder initiating the payment
     * @param beneficiary   the beneficiary to receive the payment
     * @param amount        the amount to transfer
     * @param status        current status of the instruction
     */
    public PaymentInstruction(AccountHolder accountHolder, Beneficiary beneficiary, Double amount, InstructionStatus status, String remarks) {
        this.accountHolder = accountHolder;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.status = status;
        this.remarks = remarks;
    }

    /** @return payment instruction ID */
    public Long getId() {
        return id;
    }

    /** @return account holder initiating the payment */
    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    /** @param accountHolder account holder to set */
    public void setAccountHolder(AccountHolder accountHolder) {
        this.accountHolder = accountHolder;
    }

    /** @return beneficiary receiving the payment */
    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    /** @param beneficiary beneficiary to set */
    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    /** @return amount to be transferred */
    public Double getAmount() {
        return amount;
    }

    /** @param amount amount to set */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /** @return current status */
    public InstructionStatus getStatus() {
        return status;
    }

    /** @param status status to set */
    public void setStatus(InstructionStatus status) {
        this.status = status;
    }
    
    /** @return remarks */
	public String getRemarks() {
		return remarks;
	}
	
	/** @param remarks */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
