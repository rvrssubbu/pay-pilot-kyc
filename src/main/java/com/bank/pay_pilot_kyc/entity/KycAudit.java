package com.bank.pay_pilot_kyc.entity;

import com.bank.pay_pilot_kyc.domain.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantId;

    @Enumerated(EnumType.STRING)
    private KycStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private KycStatus newStatus;

    private String updatedBy;

    private LocalDateTime timestamp;
}