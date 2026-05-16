package com.bank.pay_pilot_kyc.scheduler;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.enums.OutboxStatus;
import com.bank.pay_pilot_kyc.service.OutboxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxSchedulerTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private OutboxScheduler scheduler;

    @Test
    void shouldProcessPendingEvents() {

        OutboxEvent event1 = new OutboxEvent(
                "1",
                "KYC_UPDATED",
                "payload1",
                OutboxStatus.PENDING,
                0,
                LocalDateTime.now()
        );

        OutboxEvent event2 = new OutboxEvent(
                "2",
                "KYC_UPDATED",
                "payload2",
                OutboxStatus.PENDING,
                0,
                LocalDateTime.now()
        );

        when(outboxService.getPendingEvents())
                .thenReturn(List.of(event1, event2));

        scheduler.processPendingEvents();

        verify(outboxService, times(1))
                .getPendingEvents();

        verify(outboxService, times(1))
                .processEvent(event1);

        verify(outboxService, times(1))
                .processEvent(event2);
    }

    @Test
    void shouldNotProcessWhenNoPendingEvents() {

        when(outboxService.getPendingEvents())
                .thenReturn(List.of());

        scheduler.processPendingEvents();

        verify(outboxService, times(1))
                .getPendingEvents();

        verify(outboxService, never())
                .processEvent(any());
    }
}