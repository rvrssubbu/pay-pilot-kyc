package com.bank.pay_pilot_kyc.repository;

import com.bank.pay_pilot_kyc.domain.KycDetails;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class KycRepository {
    private final Map<String, KycDetails> store = new ConcurrentHashMap<>();

    public KycDetails save(KycDetails kycDetails){
        store.put(kycDetails.merchantId(),kycDetails);
        return kycDetails;
    }

    public Optional<KycDetails> findByMerchantId(String merchantId){
        return Optional.ofNullable(store.get(merchantId));
    }
}
