package com.bank.pay_pilot_kyc.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldHandleInvalidKycTransition() {

        InvalidKycTransitionException ex =
                new InvalidKycTransitionException(
                        "Invalid transition"
                );

        ResponseEntity<Map<String, String>> response =
                handler.handleInvalidKycTransition(ex);

        assertEquals(
                400,
                response.getStatusCodeValue()
        );

        assertEquals(
                "Invalid transition",
                response.getBody().get("error")
        );
    }

    @Test
    void shouldHandleIllegalArgument() {

        IllegalArgumentException ex =
                new IllegalArgumentException(
                        "Bad request"
                );

        ResponseEntity<Map<String, String>> response =
                handler.handleIllegalArg(ex);

        assertEquals(
                400,
                response.getStatusCodeValue()
        );

        assertEquals(
                "Bad request",
                response.getBody().get("error")
        );
    }
}