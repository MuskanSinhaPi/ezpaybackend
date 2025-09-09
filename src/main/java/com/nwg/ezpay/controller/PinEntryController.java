//package com.nwg.ezpay.controller;
//
//import com.nwg.ezpay.entity.PinEntry;
//import com.nwg.ezpay.service.PinEntryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
///**
// * REST controller for PIN entry.
// * Stores PIN securely in hashed format using BCrypt.
// * Author: Aziz Mehevi
// */
//@RestController
//@RequestMapping("/api/pin")
//public class PinEntryController {
//
//    @Autowired
//    private PinEntryService pinService;
//
//    /** Save new PIN (hashed in DB) */
//    @PostMapping("/save")
//    public ResponseEntity<PinEntry> savePin(@RequestBody Map<String, String> request) {
//        String pin = request.get("pin");
//        PinEntry saved = pinService.savePin(pin);
//        return ResponseEntity.ok(saved);
//    }
//
//    /** Verify PIN against stored hash */
//    @PostMapping("/verify/{id}")
//    public ResponseEntity<String> verifyPin(
//            @PathVariable Long id,
//            @RequestBody Map<String, String> request) {
//        String pin = request.get("pin");
//        boolean match = pinService.verifyPin(id, pin);
//        if (match) return ResponseEntity.ok("PIN verified successfully ✅");
//        return ResponseEntity.badRequest().body("Invalid PIN ❌");
//    }
//}
