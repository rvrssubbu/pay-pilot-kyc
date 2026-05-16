package com.bank.pay_pilot_kyc.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            InvalidKycTransitionException.class
    )
    public ResponseEntity<Map<String, String>>
    handleInvalidKycTransition(
            InvalidKycTransitionException ex
    ) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>>
    handleIllegalArg(
            IllegalArgumentException ex
    ) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error",
                        ex.getMessage()
                ));
    }
}