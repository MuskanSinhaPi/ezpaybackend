//package com.nwg.ezpay.service;
//
//import com.nwg.ezpay.entity.PinEntry;
//import com.nwg.ezpay.repository.PinEntryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
///**
// * Service layer for securely saving and verifying PINs.
// * Author: Aziz Mehevi
// */
//@Service
//public class PinEntryService {
//
//    @Autowired
//    private PinEntryRepository pinRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    /** Save a new PIN (hashed) */
//    public PinEntry savePin(String rawPin) {
//        PinEntry entry = new PinEntry();
//        entry.setHashedPin(passwordEncoder.encode(rawPin));
//        return pinRepo.save(entry);
//    }
//
//    /** Verify if entered PIN matches stored one */
//    public boolean verifyPin(Long id, String rawPin) {
//        Optional<PinEntry> stored = pinRepo.findById(id);
//        return stored.isPresent() &&
//               passwordEncoder.matches(rawPin, stored.get().getHashedPin());
//    }
//}
