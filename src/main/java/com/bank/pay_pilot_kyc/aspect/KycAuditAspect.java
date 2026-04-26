package com.bank.pay_pilot_kyc.aspect;

import com.bank.pay_pilot_kyc.annotation.AuditKyc;
import com.bank.pay_pilot_kyc.domain.KycAudit;
import com.bank.pay_pilot_kyc.domain.KycDetails;
import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class KycAuditAspect {

    private final KycRepository kycRepository;
    private final KycAuditRepository kycAuditRepository;

    //@Around("@annotation(AuditKyc)")
    //@AuditKyc
    //com.bank.pay_pilot_kyc.annotation.
    //@Around("@annotation(AuditKyc)")
    @Around("@annotation(com.bank.pay_pilot_kyc.annotation.AuditKyc)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {

     Object[] args = joinPoint.getArgs();
     String merchantId = (String) args[0];
     KycStatus newStatus = (KycStatus) args[1];

     System.out.println("🔥 AOP CALLED");

     KycStatus oldStatus = kycRepository.findByMerchantId(merchantId)
             .map(KycDetails::status)
             .orElse(KycStatus.PENDING);

     Object result = joinPoint.proceed();

        KycAudit audit = new KycAudit(
                merchantId,
                oldStatus,
                newStatus,
                "SYSTEM",
                LocalDateTime.now()
                );
        System.out.println("Before Save");
        kycAuditRepository.save(audit);
        System.out.println("After Save");

        return result;
    }

}
