package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControlloScadenze {
    private final TicketService ticketService;

    @Scheduled(fixedRateString = "${scheduler.check}")
    public void controlloScadenze(){ticketService.controlloScadenze();}
}
