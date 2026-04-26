package com.bank.pay_pilot_kyc.domain;

import java.time.LocalDateTime;

public record KycAudit(
        String merchantId,
        KycStatus oldStatus,
        KycStatus newStatus,
        String updatedBy,
        LocalDateTime timestamp
) {
}
