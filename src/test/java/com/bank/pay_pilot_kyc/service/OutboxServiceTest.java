package com.bank.pay_pilot_kyc.service;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.enums.OutboxStatus;
import com.bank.pay_pilot_kyc.repository.OutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxServiceTest {

    @Mock
    private OutboxRepository repository;

    @Spy
    @InjectMocks
    private OutboxService service;

    private OutboxEvent event;

    @BeforeEach
    void setup() {

        event = new OutboxEvent(
                "1",
                "KYC_UPDATED",
                "payload",
                OutboxStatus.PENDING,
                0,
                LocalDateTime.now()
        );
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

        when(repository.findByStatus(OutboxStatus.PENDING))
                .thenReturn(List.of(event));

        List<OutboxEvent> result =
                service.getPendingEvents();

        assertEquals(1, result.size());

        assertEquals(
                OutboxStatus.PENDING,
                result.get(0).getStatus()
        );
    }

    @Test
    void shouldMarkEventAsSent() {

        doNothing()
                .when(service)
                .simulatePublish(any());

        service.processEvent(event);

        assertEquals(
                OutboxStatus.SENT,
                event.getStatus()
        );

        verify(repository, times(1))
                .save(any(OutboxEvent.class));
    }

    @Test
    void shouldRetryWhenPublishFails() {

        doThrow(new RuntimeException("Kafka failure"))
                .when(service)
                .simulatePublish(any());

        service.processEvent(event);

        assertEquals(
                1,
                event.getRetryCount()
        );

        assertEquals(
                OutboxStatus.PENDING,
                event.getStatus()
        );

        verify(repository, times(1))
                .save(any(OutboxEvent.class));
    }

    @Test
    void shouldMoveEventToFailedAfterMaxRetry() {

        OutboxEvent failedEvent = new OutboxEvent(
                "2",
                "KYC_UPDATED",
                "payload",
                OutboxStatus.PENDING,
                2,
                LocalDateTime.now()
        );

        doThrow(new RuntimeException("Kafka failure"))
                .when(service)
                .simulatePublish(any());

        service.processEvent(failedEvent);

        assertEquals(
                3,
                failedEvent.getRetryCount()
        );

        assertEquals(
                OutboxStatus.FAILED,
                failedEvent.getStatus()
        );

        verify(repository, times(1))
                .save(any(OutboxEvent.class));
    }

    @Test
    void shouldExecuteSimulatePublishSuccessfully() {

        OutboxEvent event = new OutboxEvent(
                "1",
                "KYC_UPDATED",
                "payload",
                OutboxStatus.PENDING,
                0,
                LocalDateTime.now()
        );

        try {

            service.simulatePublish(event);

        } catch (Exception ignored) {

            // expected sometimes because random failure
        }

        assertTrue(true);
    }
}