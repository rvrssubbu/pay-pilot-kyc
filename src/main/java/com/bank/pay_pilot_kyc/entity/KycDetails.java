package com.bank.pay_pilot_kyc.entity;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycDetails {

    @Id
    private String merchantId;

    @Enumerated(EnumType.STRING)
    private KycStatus status;

    private String updatedBy;
}