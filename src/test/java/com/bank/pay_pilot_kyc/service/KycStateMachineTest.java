package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import com.bank.pay_pilot_kyc.exception.InvalidKycTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KycStateMachineTest {

    private KycStateMachine kycStateMachine;

    @BeforeEach
    void setup() {

        kycStateMachine = new KycStateMachine();
    }

    @Test
    void shouldAllowPendingToVerifiedTransition() {

        assertDoesNotThrow(() ->
                kycStateMachine.validate(
                        KycStatus.PENDING,
                        KycStatus.VERIFIED
                )
        );
    }

    @Test
    void shouldAllowPendingToRejectedTransition() {

        assertDoesNotThrow(() ->
                kycStateMachine.validate(
                        KycStatus.PENDING,
                        KycStatus.REJECTED
                )
        );
    }

    @Test
    void shouldThrowExceptionForVerifiedToPending() {

        InvalidKycTransitionException exception =
                assertThrows(
                        InvalidKycTransitionException.class,
                        () -> kycStateMachine.validate(
                                KycStatus.VERIFIED,
                                KycStatus.PENDING
                        )
                );

        assertEquals(
                "Invalid transition from VERIFIED to PENDING",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionForRejectedToVerified() {

        InvalidKycTransitionException exception =
                assertThrows(
                        InvalidKycTransitionException.class,
                        () -> kycStateMachine.validate(
                                KycStatus.REJECTED,
                                KycStatus.VERIFIED
                        )
                );

        assertEquals(
                "Invalid transition from REJECTED to VERIFIED",
                exception.getMessage()
        );
    }
}