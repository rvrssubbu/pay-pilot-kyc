package com.bank.pay_pilot_kyc.controller;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.entity.KycAudit;
import com.bank.pay_pilot_kyc.entity.KycDetails;
import com.bank.pay_pilot_kyc.service.KycService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC APIs",
        description = "KYC status and audit APIs")
public class KycController {

    private final KycService service;


    @PostMapping("/update")
    @Operation(
            summary = "Update merchant KYC status",
            description = "Updates merchant KYC state and creates audit + outbox event"
    )
    public ResponseEntity<KycDetails> update(
            @RequestParam String merchantId,
            @RequestParam KycStatus status) {

        return ResponseEntity.ok(service.updateStatus(merchantId, status));
    }

    @GetMapping("/history/{merchantId}")
    public ResponseEntity<List<KycAudit>> getHistory(
            @PathVariable String merchantId){
        return ResponseEntity.ok(service.getHistory(merchantId));
    }

    @GetMapping("history")
    public ResponseEntity<List<KycAudit>> getCompleteHistory(){
        return ResponseEntity.ok(service.getCompleteHistory());
    }

}