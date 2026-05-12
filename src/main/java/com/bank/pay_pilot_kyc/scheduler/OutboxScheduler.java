package com.bank.pay_pilot_kyc.scheduler;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 5000)
    public void processPendingEvents() {

       /* log.info("Scheduler started processing pending events");

        outboxService.getPendingEvents()
                .forEach(outboxService::processEvent);*/

        List<OutboxEvent> events = outboxService.getPendingEvents();

        if(events.isEmpty()) {
            return;
        }

        log.info("Processing {} pending events", events.size());

        events.forEach(outboxService::processEvent);

    }
}