//package com.nwg.ezpay.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "pin_entry")
//public class PinEntry {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    /** Securely hashed PIN (using BCrypt) */
//    private String hashedPin;
//
//    // getters and setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getHashedPin() { return hashedPin; }
//    public void setHashedPin(String hashedPin) { this.hashedPin = hashedPin; }
//}
