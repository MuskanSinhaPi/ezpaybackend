package com.nwg.ezpay.controller;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.entity.Beneficiary;
import com.nwg.ezpay.entity.InstructionStatus;
import com.nwg.ezpay.entity.PaymentInstruction;
import com.nwg.ezpay.service.PaymentInstructionService;
import com.nwg.ezpay.service.AccountHolderService;
import com.nwg.ezpay.service.BeneficiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * REST controller for PaymentInstruction entity.
 * Handles creation, fetching, deletion, and mock execution of payment instructions.
 *
 * Author: Muskan
 * Version: 1.2
 * Since: 2025-08-25
 * Revised: 2025-08-26 - fully realistic operations
 * Revised: 2025-09-05 - Add endpoint /execute to trigger mock transfer, Updated endpoints to resolve holder from query parameter @username
 */
@RestController
@RequestMapping("/api/payment-instructions")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentInstructionController {

    private final PaymentInstructionService paymentInstructionService;
    private final AccountHolderService accountHolderService;
    private final BeneficiaryService beneficiaryService;

    public PaymentInstructionController(PaymentInstructionService paymentInstructionService,
                                        AccountHolderService accountHolderService,
                                        BeneficiaryService beneficiaryService) {
        this.paymentInstructionService = paymentInstructionService;
        this.accountHolderService = accountHolderService;
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentInstruction>> getAllInstructions() {
        return new ResponseEntity<>(paymentInstructionService.getAllInstructions(), HttpStatus.OK);
    }

    @GetMapping("/account-holder")
    public ResponseEntity<List<PaymentInstruction>> getByAccountHolder(@RequestParam String username) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        List<PaymentInstruction> instructions =
            paymentInstructionService.getInstructionsByAccountHolder(holder.getId());
        return new ResponseEntity<>(instructions, HttpStatus.OK);
    }

    @PostMapping("/account-holder/{accountHolderId}/beneficiary/{beneficiaryId}")
    public ResponseEntity<PaymentInstruction> createInstruction(@RequestParam String username,
                                                                @PathVariable Long beneficiaryId,
                                                                @RequestBody PaymentInstruction instruction) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        Beneficiary beneficiary = beneficiaryService.getBeneficiaryByIdAndAccountHolder(beneficiaryId, holder);
        instruction.setAccountHolder(holder);
        instruction.setBeneficiary(beneficiary);
        return new ResponseEntity<>(paymentInstructionService.saveInstruction(instruction), HttpStatus.CREATED);
    }

    @DeleteMapping("/account-holder/{accountHolderId}/instruction/{instructionId}")
    public ResponseEntity<String> deleteInstruction(@RequestParam String username,
                                                    @PathVariable Long instructionId) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        paymentInstructionService.deleteInstruction(instructionId, holder);
        return new ResponseEntity<>("Payment instruction deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentInstruction>> getByStatus(@PathVariable InstructionStatus status) {
        List<PaymentInstruction> instructions = paymentInstructionService.getInstructionsByStatus(status);
        return new ResponseEntity<>(instructions, HttpStatus.OK);
    }

    @GetMapping("/account-holder/status/{status}")
    public ResponseEntity<List<PaymentInstruction>> getByAccountHolderAndStatus(@RequestParam String username,
                                                                                @PathVariable InstructionStatus status) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        List<PaymentInstruction> instructions =
            paymentInstructionService.getInstructionsByAccountHolderAndStatus(holder.getId(), status);
        return new ResponseEntity<>(instructions, HttpStatus.OK);
    }

    @PutMapping("/account-holder/{accountHolderId}/instruction/{instructionId}/status/{status}")
    public ResponseEntity<PaymentInstruction> updateStatus(@RequestParam String username,
                                                           @PathVariable Long instructionId,
                                                           @PathVariable InstructionStatus status) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        PaymentInstruction updated =
            paymentInstructionService.updateInstructionStatus(instructionId, holder, status);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/account-holder/{accountHolderId}/instruction/{instructionId}/execute")
    public ResponseEntity<PaymentInstruction> executeMockTransfer(@RequestParam String username,
                                                                  @PathVariable Long instructionId) {
        AccountHolder holder = accountHolderService.getByUsername(username);
        PaymentInstruction updated = paymentInstructionService.performMockTransfer(instructionId, holder);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    /* ---------------- Authentication-based endpoints (commented out) ----------------
    
    @GetMapping("/account-holder")
    public ResponseEntity<List<PaymentInstruction>> getByAccountHolderAuth() {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        List<PaymentInstruction> instructions =
            paymentInstructionService.getInstructionsByAccountHolder(holder.getId());
        return new ResponseEntity<>(instructions, HttpStatus.OK);
    }

    @PostMapping("/account-holder/beneficiary/{beneficiaryId}")
    public ResponseEntity<PaymentInstruction> createInstructionAuth(@PathVariable Long beneficiaryId,
                                                                    @RequestBody PaymentInstruction instruction) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        Beneficiary beneficiary = beneficiaryService.getBeneficiaryByIdAndAccountHolder(beneficiaryId, holder);
        instruction.setAccountHolder(holder);
        instruction.setBeneficiary(beneficiary);
        return new ResponseEntity<>(paymentInstructionService.saveInstruction(instruction), HttpStatus.CREATED);
    }

    @DeleteMapping("/account-holder/instruction/{instructionId}")
    public ResponseEntity<String> deleteInstructionAuth(@PathVariable Long instructionId) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        paymentInstructionService.deleteInstruction(instructionId, holder);
        return new ResponseEntity<>("Payment instruction deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/account-holder/instruction/{instructionId}/status/{status}")
    public ResponseEntity<PaymentInstruction> updateStatusAuth(@PathVariable Long instructionId,
                                                               @PathVariable InstructionStatus status) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        PaymentInstruction updated =
            paymentInstructionService.updateInstructionStatus(instructionId, holder, status);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/account-holder/instruction/{instructionId}/execute")
    public ResponseEntity<PaymentInstruction> executeMockTransferAuth(@PathVariable Long instructionId) {
        AccountHolder holder = SecurityUtil.getCurrentAccountHolder();
        PaymentInstruction updated = paymentInstructionService.performMockTransfer(instructionId, holder);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    ------------------------------------------------------------------------------- */
}