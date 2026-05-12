package com.bank.pay_pilot_kyc.repository;

import com.bank.pay_pilot_kyc.entity.KycAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface KycAuditRepository
        extends JpaRepository<KycAudit, Long> {

    List<KycAudit> findByMerchantId(String merchantId);
}
