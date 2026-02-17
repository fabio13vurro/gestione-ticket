package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.services.OverSlaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OverSlaScheduler {
    private final OverSlaService overSlaService;

    @Scheduled(fixedRateString = "${scheduler.fixedrate}")
    public void controlloOverSla(){
        overSlaService.controlloOverSla();
    }
}