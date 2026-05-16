package com.bank.pay_pilot_kyc.repository;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.domain.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository
        extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent> findByStatus(
            OutboxStatus status
    );

    boolean existsByEventId(String eventId);

}
