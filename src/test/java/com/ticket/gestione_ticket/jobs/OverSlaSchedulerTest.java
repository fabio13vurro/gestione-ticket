package com.ticket.gestione_ticket.jobs;

import static org.mockito.Mockito.*;

import com.ticket.gestione_ticket.services.OverSlaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OverSlaSchedulerTest {

    @Mock
    private OverSlaService overSlaService;

    @InjectMocks
    private OverSlaScheduler scheduler;

    @Test
    void controlloOverSla_shouldCallService() {
        scheduler.controlloOverSla();

        verify(overSlaService, times(1)).controlloOverSla();
    }
}