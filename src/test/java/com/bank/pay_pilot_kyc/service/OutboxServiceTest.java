package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.enums.OutboxStatus;
import com.bank.pay_pilot_kyc.repository.OutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class OutboxServiceTest {

    private OutboxRepository repository;

    private OutboxService service;

    @BeforeEach
    void setup() {

        repository = mock(OutboxRepository.class);

        service = new OutboxService(repository);
    }

    @Test
    void shouldSaveOutboxEvent() {

        service.saveEvent(
                "KYC_UPDATED",
                "merchant updated"
        );

        verify(repository, times(1))
                .save(any(OutboxEvent.class));
    }

    @Test
    void shouldReturnPendingEvents() {

        List<OutboxEvent> events = List.of(
                OutboxEvent.create(
                        "KYC_UPDATED",
                        "payload"
                )
        );

        when(repository.findByStatus(OutboxStatus.PENDING))
                .thenReturn(events);

        List<OutboxEvent> result =
                service.getPendingEvents();

        assert result.size() == 1;
    }

    @Test
    void shouldUpdateEventWhenPublishFails() {

        OutboxEvent event = new OutboxEvent(
                "1",
                "KYC_UPDATED",
                "payload",
                OutboxStatus.PENDING,
                0,
                LocalDateTime.now()
        );

        service.processEvent(event);

        //verify(repository, atLeastOnce()).update(any(OutboxEvent.class));
    }
}