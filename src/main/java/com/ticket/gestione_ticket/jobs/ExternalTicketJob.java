package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalTicketJob {

    private final TicketService ticketService;

    //@Scheduled(fixedRateString = "${scheduler.fixedrate}")
    public void creazioneTicket() {

        for (int i = 1; i <= 5; i++) {
            var t = ticketService.creazioneTicket(
                    "Ticket esterno automatico numero ",
                    "Creato dal job schedulato"
            );
        }
    }
}