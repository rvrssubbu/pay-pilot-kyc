package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.constants.KafkaTopicsConstants;
import com.bank.pay_pilot_kyc.domain.OutboxStatus;
import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository repository;

    private static final int MAX_RETRY = 3;

    public void saveEvent(
            String eventType,
            String payload
    ) {

        OutboxEvent event =
                OutboxEvent.create(
                        eventType,
                        payload
                );

        repository.save(event);

        log.info(
                "Outbox event saved: {}",
                event.getEventId()
        );
    }

    public List<OutboxEvent> getPendingEvents() {

        return repository.findByStatus(
                OutboxStatus.PENDING
        );
    }

    public void processEvent(
            OutboxEvent event
    ) {

        try {

            publishToKafka(event);

            event.setStatus(
                    OutboxStatus.SENT
            );

            repository.save(event);

            log.info(
                    "Event published successfully: {}",
                    event.getEventId()
            );

            log.info(
                    "eventId={} used as dedupe key",
                    event.getEventId()
            );

        } catch (Exception ex) {

            int nextRetry =
                    event.getRetryCount() + 1;

            event.setRetryCount(nextRetry);

            if (nextRetry >= MAX_RETRY) {

                event.setStatus(
                        OutboxStatus.DLQ
                );

                log.error(
                        "Event moved to DLQ topic={} eventId={}",
                        KafkaTopicsConstants.KYC_DLQ_TOPIC,
                        event.getEventId()
                );

            } else {

                event.setStatus(
                        OutboxStatus.PENDING
                );

                log.warn(
                        "Retrying event through topic={} retryCount={}",
                        KafkaTopicsConstants.KYC_RETRY_TOPIC,
                        nextRetry
                );
            }

            repository.save(event);

            log.error(
                    "Publish failed for event {} retry {}",
                    event.getEventId(),
                    nextRetry
            );
        }
    }

    public void publishToKafka(
            OutboxEvent event
    ) {

        log.info(
                "Publishing event to topic={} eventType={}",
                KafkaTopicsConstants.KYC_EVENTS_TOPIC,
                event.getEventType()
        );

        boolean randomFailure =
                new Random().nextBoolean();

        if (true) {//randomFailure

            throw new RuntimeException(
                    "Temporary publish failure"
            );
        }
    }
}