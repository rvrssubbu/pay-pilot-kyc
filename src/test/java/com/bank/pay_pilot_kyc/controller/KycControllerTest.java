package com.bank.pay_pilot_kyc.controller;

import com.bank.pay_pilot_kyc.entity.KycAudit;
import com.bank.pay_pilot_kyc.entity.KycDetails;
import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.service.KycService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KycControllerTest {

    @Mock
    private KycService service;

    @InjectMocks
    private KycController controller;

    @Test
    void shouldUpdateKycStatus() {

        KycDetails details =
                KycDetails.builder()
                        .merchantId("M101")
                        .status(KycStatus.VERIFIED)
                        .updatedBy("SYSTEM")
                        .build();

        when(service.updateStatus(
                "M101",
                KycStatus.VERIFIED
        )).thenReturn(details);

        ResponseEntity<KycDetails> response =
                controller.update(
                        "M101",
                        KycStatus.VERIFIED
                );

        assertEquals(
                200,
                response.getStatusCodeValue()
        );

        assertEquals(
                KycStatus.VERIFIED,
                response.getBody().getStatus()
        );
    }

    @Test
    void shouldReturnMerchantHistory() {

        List<KycAudit> audits = List.of();

        when(service.getHistory("M101"))
                .thenReturn(audits);

        ResponseEntity<List<KycAudit>> response =
                controller.getHistory("M101");

        assertEquals(
                200,
                response.getStatusCodeValue()
        );
    }

    @Test
    void shouldReturnCompleteHistory() {

        List<KycAudit> audits = List.of();

        when(service.getCompleteHistory())
                .thenReturn(audits);

        ResponseEntity<List<KycAudit>> response =
                controller.getCompleteHistory();

        assertEquals(
                200,
                response.getStatusCodeValue()
        );
    }
}