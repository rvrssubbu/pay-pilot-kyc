package com.bank.pay_pilot_kyc.controller;

import com.bank.pay_pilot_kyc.entity.OutboxEvent;
import com.bank.pay_pilot_kyc.repository.OutboxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxControllerTest {

    @Mock
    private OutboxRepository repository;

    @InjectMocks
    private OutboxController controller;

    @Test
    void shouldReturnAllOutboxEvents() {

        when(repository.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<OutboxEvent>> response =
                controller.getAllEvents();

        assertEquals(
                200,
                response.getStatusCodeValue()
        );
    }
}