package com.bank.pay_pilot_kyc.entity;

import com.bank.pay_pilot_kyc.domain.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    private String eventId;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private int retryCount;

    private LocalDateTime createdAt;

    public static OutboxEvent create(
            String eventType,
            String payload
    ) {

        return OutboxEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}