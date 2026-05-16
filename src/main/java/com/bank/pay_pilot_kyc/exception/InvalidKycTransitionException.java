package com.bank.pay_pilot_kyc.exception;

public class InvalidKycTransitionException
        extends RuntimeException {

    public InvalidKycTransitionException(
            String message
    ) {
        super(message);
    }
}