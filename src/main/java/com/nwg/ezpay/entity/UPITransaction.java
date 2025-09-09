package com.nwg.ezpay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity class representing a UPI (Unified Payments Interface) transaction.
 * This class maps to the 'upi_transactions' table in the database.
 * 
 * Author : Vaishali Aggarwal
 */
@Entity
@Table(name = "upi_transactions")
public class UPITransaction {

    /** Unique identifier for the transaction */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /** UPI ID of the sender account */
    @Column(name = "SENDER_UPI_ID", nullable = false)
    private String senderUpiId;

    /** UPI ID of the receiver account */
    @Column(name = "RECEIVER_UPI_ID", nullable = false)
    private String receiverUpiId;

    /** Transaction amount */
    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private Integer amount;

    /** Optional remarks for the transaction */
    @Column(name = "REMARKS")
    private String remarks;

    /** Transaction status (SUCCESS / FAILED) */
    @Column(name = "STATUS", nullable = false)
    private String status;

    /** Timestamp when the transaction was created */
    @Column(name = "CREATED_AT")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Default constructor
     */
    public UPITransaction() {
    }

    /**
     * Parameterized constructor to create a UPI transaction
     * 
     * @param id            Unique identifier
     * @param senderUpiId   Sender's UPI ID
     * @param receiverUpiId Receiver's UPI ID
     * @param amount        Transaction amount
     * @param remarks       Transaction remarks
     * @param status        Transaction status
     * @param timestamp     Transaction timestamp
     */
    public UPITransaction(Long id, String senderUpiId, String receiverUpiId, Integer amount, String remarks,
            String status, LocalDateTime timestamp) {
        this.id = id;
        this.senderUpiId = senderUpiId;
        this.receiverUpiId = receiverUpiId;
        this.amount = amount;
        this.remarks = remarks;
        this.status = status;
        this.timestamp = timestamp;
    }

    /**
     * @return the transaction ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the transaction ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the sender's UPI ID
     */
    public String getSenderUpiId() {
        return senderUpiId;
    }

    /**
     * @param senderUpiId the sender's UPI ID to set
     */
    public void setSenderUpiId(String senderUpiId) {
        this.senderUpiId = senderUpiId;
    }

    /**
     * @return the receiver's UPI ID
     */
    public String getReceiverUpiId() {
        return receiverUpiId;
    }

    /**
     * @param receiverUpiId the receiver's UPI ID to set
     */
    public void setReceiverUpiId(String receiverUpiId) {
        this.receiverUpiId = receiverUpiId;
    }

    /**
     * @return the transaction amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the transaction amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return the transaction remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the transaction remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the transaction status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the transaction status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the transaction timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the transaction timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}