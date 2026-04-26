package com.bank.pay_pilot_kyc.domain;

public record KycDetails(
        String merchantId,
        KycStatus status
) {

}
