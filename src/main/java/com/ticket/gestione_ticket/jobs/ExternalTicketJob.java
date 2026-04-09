package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalTicketJob {

    private final TicketService ticketService;
    private final CommentoService commentoService;

    //@Scheduled(fixedDelayString = "${scheduler.create}")
    public void creazioneTicket() {
        for (int i = 1; i <= 100; i++) {
            Ticket t = ticketService.creazioneTicket();
            commentoService.creazioneCommenti(t);
        }
    }

    @Scheduled(fixedDelayString = "${scheduler.create}")
    public void creazioneTicketVecchi(){
        for (int i = 1; i <= 100; i++) {
            Ticket t = ticketService.creazioneTicketVecchi();
        }
    }
}