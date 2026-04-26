/*
package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.domain.KycDetails;
import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.repository.KycAuditRepository;
import com.bank.pay_pilot_kyc.repository.KycRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KycServiceTest {

    @Mock
    private KycRepository kycRepository;

    @Mock
    private KycAuditRepository auditRepository;

    @Mock
    private KycStateMachine kycStateMachine;

    @InjectMocks
    private KycService service;

    @Test
    void shouldUpdateStatusSuccessfully() {

        String merchantId = "123";

        when(kycRepository.findByMerchantId(merchantId))
                .thenReturn(Optional.of(new KycDetails(merchantId, KycStatus.PENDING)));

        KycDetails result = service.updateStatus(merchantId, KycStatus.VERIFIED);

        assertEquals(KycStatus.VERIFIED, result.status());
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {

        String merchantId = "123";

        when(kycRepository.findByMerchantId(merchantId))
                .thenReturn(Optional.of(new KycDetails(merchantId, KycStatus.VERIFIED)));

        assertThrows(IllegalStateException.class, () ->
                service.updateStatus(merchantId, KycStatus.PENDING));
    }

}*/
