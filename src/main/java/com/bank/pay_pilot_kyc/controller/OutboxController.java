package com.bank.pay_pilot_kyc.controller;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/outbox")
@RequiredArgsConstructor
public class OutboxController {

    private final OutboxRepository repository;

    @GetMapping
    public ResponseEntity<List<OutboxEvent>> getAllEvents() {
        return ResponseEntity.ok(repository.findAll());
    }
}