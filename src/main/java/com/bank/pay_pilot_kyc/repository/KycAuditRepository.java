package com.bank.pay_pilot_kyc.repository;

import com.bank.pay_pilot_kyc.domain.KycAudit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class KycAuditRepository {

    private final List<KycAudit> audits = new CopyOnWriteArrayList<>();

    public void save(KycAudit kycAudit){
        System.out.println("Saving audit: " + kycAudit);
        audits.add(kycAudit);
    }

    public List<KycAudit> findByMerchantId(String merchantId){
        return audits.stream()
                .filter(a->a.merchantId().equals(merchantId))
                .toList();
    }

    public List<KycAudit> findAllMerchants(){
        return audits.stream()
                .toList();
    }
}
