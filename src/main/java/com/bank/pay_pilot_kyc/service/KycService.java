
package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.annotation.AuditKyc;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.entity.KycAudit;
import com.bank.pay_pilot_kyc.entity.KycDetails;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycRepository kycRepository;

    private final KycStateMachine kycStateMachine;

    private final KycAuditRepository kycAuditRepository;

    private final OutboxService outboxService;

    @Transactional
    @AuditKyc
    public KycDetails updateStatus(
            String merchantId,
            KycStatus newStatus
    ) {

        System.out.println("Service method called");

        KycDetails existing =
                kycRepository.findById(merchantId)
                        .orElseGet(() -> {

                            KycDetails newKyc =
                                    new KycDetails();

                            newKyc.setMerchantId(merchantId);

                            newKyc.setStatus(KycStatus.PENDING);

                            newKyc.setUpdatedBy("SYSTEM");

                            return newKyc;
                        });

        kycStateMachine.validate(
                existing.getStatus(),
                newStatus
        );

        existing.setStatus(newStatus);

        existing.setUpdatedBy("SYSTEM");

        outboxService.saveEvent(
                "KYC_UPDATED",
                merchantId + " updated to " + newStatus
        );

        return kycRepository.save(existing);
    }

    public KycDetails getCurrentStatus(
            String merchantId
    ) {

        return kycRepository.findById(merchantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KYC not found for merchantId: "
                                        + merchantId
                        ));
    }

    public List<KycAudit> getHistory(
            String merchantId
    ) {

        return kycAuditRepository
                .findByMerchantId(merchantId);
    }

    public List<KycAudit> getCompleteHistory() {

        return kycAuditRepository.findAll();
    }
}