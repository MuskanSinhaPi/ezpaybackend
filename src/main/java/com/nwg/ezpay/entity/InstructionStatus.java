package com.nwg.ezpay.entity;

/**
 * Lifecycle states of a payment instruction.
 * 
 * Author: Muskan
 * Version: 2.0
 * Date: 2025-09-01
 */
public enum InstructionStatus {
    DRAFT,       // Created but not validated
    VALIDATED,   // Validation checks passed
    SUBMITTED,   // Submitted to bank rails
    SUCCESS,     // Confirmed successful settlement
    REJECTED,    // Failed by validation or bank
    CANCELLED    // User cancelled before submission
}
