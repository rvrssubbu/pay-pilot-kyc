package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class KycStateMachine {
    private final Map<KycStatus, Set<KycStatus>> transitions =Map.of(
            KycStatus.PENDING, Set.of(KycStatus.VERIFIED, KycStatus.REJECTED),
            KycStatus.VERIFIED, Set.of(),
            KycStatus.REJECTED, Set.of()
    );

    public void validate(KycStatus current, KycStatus next){
        if(!transitions.getOrDefault(current, Set.of()).contains(next)){
            throw new IllegalStateException("Invalid transition from "+current+" to "+next);
        }
    }
}
