
package com.bank.pay_pilot_kyc.service;


import com.bank.pay_pilot_kyc.entity.KycAudit;
import com.bank.pay_pilot_kyc.entity.KycDetails;
import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KycServiceTest {

    @Mock
    private KycRepository kycRepository;

    @Mock
    private KycAuditRepository kycAuditRepository;

    @Mock
    private KycStateMachine kycStateMachine;

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private KycService kycService;

    private KycDetails existingKyc;

    @BeforeEach
    void setup() {

        existingKyc = KycDetails.builder()
                .merchantId("M101")
                .status(KycStatus.PENDING)
                .updatedBy("SYSTEM")
                .build();
    }

    @Test
    void shouldUpdateKycStatusSuccessfully() {

        when(kycRepository.findById("M101"))
                .thenReturn(Optional.of(existingKyc));

        when(kycRepository.save(any(KycDetails.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        KycDetails result =
                kycService.updateStatus(
                        "M101",
                        KycStatus.VERIFIED
                );

        assertNotNull(result);

        assertEquals(
                KycStatus.VERIFIED,
                result.getStatus()
        );

        verify(kycStateMachine)
                .validate(
                        KycStatus.PENDING,
                        KycStatus.VERIFIED
                );

        verify(outboxService)
                .saveEvent(
                        eq("KYC_UPDATED"),
                        contains("VERIFIED")
                );

        verify(kycRepository)
                .save(any(KycDetails.class));
    }

    @Test
    void shouldCreateNewKycWhenMerchantDoesNotExist() {

        when(kycRepository.findById("NEW101"))
                .thenReturn(Optional.empty());

        when(kycRepository.save(any(KycDetails.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        KycDetails result =
                kycService.updateStatus(
                        "NEW101",
                        KycStatus.VERIFIED
                );

        assertNotNull(result);

        assertEquals(
                "NEW101",
                result.getMerchantId()
        );

        assertEquals(
                KycStatus.VERIFIED,
                result.getStatus()
        );

        assertEquals(
                "SYSTEM",
                result.getUpdatedBy()
        );

        verify(kycRepository)
                .save(any(KycDetails.class));

        verify(outboxService)
                .saveEvent(
                        eq("KYC_UPDATED"),
                        contains("VERIFIED")
                );
    }

    @Test
    void shouldReturnCurrentStatus() {

        when(kycRepository.findById("M101"))
                .thenReturn(Optional.of(existingKyc));

        KycDetails result =
                kycService.getCurrentStatus("M101");

        assertNotNull(result);

        assertEquals(
                "M101",
                result.getMerchantId()
        );

        assertEquals(
                KycStatus.PENDING,
                result.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenMerchantNotFound() {

        when(kycRepository.findById("INVALID"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> kycService.getCurrentStatus("INVALID")
                );

        assertTrue(
                exception.getMessage()
                        .contains("KYC not found for merchantId: INVALID")
        );
    }

    @Test
    void shouldReturnAuditHistory() {

        KycAudit audit = KycAudit.builder()
                .merchantId("M101")
                .oldStatus(KycStatus.PENDING)
                .newStatus(KycStatus.VERIFIED)
                .updatedBy("SYSTEM")
                .build();

        when(kycAuditRepository.findByMerchantId("M101"))
                .thenReturn(List.of(audit));

        List<KycAudit> result =
                kycService.getHistory("M101");

        assertEquals(1, result.size());

        assertEquals(
                KycStatus.VERIFIED,
                result.get(0).getNewStatus()
        );
    }

    @Test
    void shouldReturnCompleteAuditHistory() {

        KycAudit audit1 = KycAudit.builder()
                .merchantId("M101")
                .oldStatus(KycStatus.PENDING)
                .newStatus(KycStatus.VERIFIED)
                .updatedBy("SYSTEM")
                .build();

        KycAudit audit2 = KycAudit.builder()
                .merchantId("M102")
                .oldStatus(KycStatus.PENDING)
                .newStatus(KycStatus.REJECTED)
                .updatedBy("SYSTEM")
                .build();

        when(kycAuditRepository.findAll())
                .thenReturn(List.of(audit1, audit2));

        List<KycAudit> result =
                kycService.getCompleteHistory();

        assertEquals(2, result.size());
    }

}
