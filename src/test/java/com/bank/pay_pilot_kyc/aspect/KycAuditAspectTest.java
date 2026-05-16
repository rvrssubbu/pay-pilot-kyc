package com.bank.pay_pilot_kyc.aspect;

import com.bank.pay_pilot_kyc.entity.KycAudit;
import com.bank.pay_pilot_kyc.entity.KycDetails;
import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KycAuditAspectTest {

    @Mock
    private KycRepository kycRepository;

    @Mock
    private KycAuditRepository kycAuditRepository;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private KycAuditAspect aspect;

    @Test
    void shouldCreateAuditRecordSuccessfully() throws Throwable {

        KycDetails existingKyc =
                KycDetails.builder()
                        .merchantId("M101")
                        .status(KycStatus.PENDING)
                        .updatedBy("SYSTEM")
                        .build();

        when(joinPoint.getArgs())
                .thenReturn(new Object[]{
                        "M101",
                        KycStatus.VERIFIED
                });

        when(kycRepository.findById("M101"))
                .thenReturn(Optional.of(existingKyc));

        when(joinPoint.proceed())
                .thenReturn("SUCCESS");

        Object result =
                aspect.audit(joinPoint);

        assertEquals("SUCCESS", result);

        ArgumentCaptor<KycAudit> captor =
                ArgumentCaptor.forClass(KycAudit.class);

        verify(kycAuditRepository)
                .save(captor.capture());

        KycAudit savedAudit =
                captor.getValue();

        assertEquals(
                "M101",
                savedAudit.getMerchantId()
        );

        assertEquals(
                KycStatus.PENDING,
                savedAudit.getOldStatus()
        );

        assertEquals(
                KycStatus.VERIFIED,
                savedAudit.getNewStatus()
        );

        assertEquals(
                "SYSTEM",
                savedAudit.getUpdatedBy()
        );
    }

    @Test
    void shouldUsePendingWhenMerchantDoesNotExist() throws Throwable {

        when(joinPoint.getArgs())
                .thenReturn(new Object[]{
                        "NEW101",
                        KycStatus.VERIFIED
                });

        when(kycRepository.findById("NEW101"))
                .thenReturn(Optional.empty());

        when(joinPoint.proceed())
                .thenReturn("SUCCESS");

        aspect.audit(joinPoint);

        ArgumentCaptor<KycAudit> captor =
                ArgumentCaptor.forClass(KycAudit.class);

        verify(kycAuditRepository)
                .save(captor.capture());

        KycAudit savedAudit =
                captor.getValue();

        assertEquals(
                KycStatus.PENDING,
                savedAudit.getOldStatus()
        );
    }
}