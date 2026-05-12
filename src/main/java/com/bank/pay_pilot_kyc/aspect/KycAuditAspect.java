package com.bank.pay_pilot_kyc.aspect;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.entity.KycAudit;
import com.bank.pay_pilot_kyc.entity.KycDetails;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KycAuditAspect {

    private final KycRepository kycRepository;

    private final KycAuditRepository kycAuditRepository;

    @Around("@annotation(com.bank.pay_pilot_kyc.annotation.AuditKyc)")
    public Object audit(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        log.info("🔥 AOP Audit Triggered");

        Object[] args = joinPoint.getArgs();

        String merchantId = (String) args[0];

        KycStatus newStatus = (KycStatus) args[1];

        KycStatus oldStatus =
                kycRepository.findById(merchantId)
                        .map(KycDetails::getStatus)
                        .orElse(KycStatus.PENDING);

        log.info(
                "KYC status transition for merchantId={} from {} to {}",
                merchantId,
                oldStatus,
                newStatus
        );

        Object result = joinPoint.proceed();

        KycAudit audit = KycAudit.builder()
                .merchantId(merchantId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy("SYSTEM")
                .timestamp(LocalDateTime.now())
                .build();

        log.info(
                "Saving audit record for merchantId={}",
                merchantId
        );

        kycAuditRepository.save(audit);

        log.info(
                "Audit saved successfully for merchantId={}",
                merchantId
        );

        return result;
    }
}