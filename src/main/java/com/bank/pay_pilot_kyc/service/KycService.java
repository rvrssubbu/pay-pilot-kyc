package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.annotation.AuditKyc;
import com.bank.pay_pilot_kyc.domain.KycAudit;
import com.bank.pay_pilot_kyc.domain.KycDetails;
import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycRepository kycRepository;
    private final KycStateMachine kycStateMachine;

    private final KycAuditRepository kycAuditRepository;

    @AuditKyc
    public KycDetails updateStatus(String merchantId, KycStatus newStatus) {
        System.out.println("Service method called");
        KycDetails existing = kycRepository.findByMerchantId(merchantId)
                .orElse(new KycDetails(merchantId, KycStatus.PENDING));

        kycStateMachine.validate(existing.status(), newStatus);

        KycDetails updated = new KycDetails(merchantId, newStatus);

        return kycRepository.save(updated);
    }


    public KycDetails getCurrentStatus(String merchantId) {
        return kycRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> new RuntimeException("kyc not found for merchantId: " + merchantId));
    }

    public List<KycAudit> getHistory(String merchantId) {
        return kycAuditRepository.findByMerchantId(merchantId);
    }

    public List<KycAudit> getCompleteHistory() {
        return kycAuditRepository.findAllMerchants();
    }
}
