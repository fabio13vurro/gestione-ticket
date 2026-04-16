package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.services.StoricoStatoService;
import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CambiaStato {
    private final TicketService ticketService;
    private final StoricoStatoService storicoStatoService;

    @Scheduled(fixedDelayString = "${scheduler.check}")
    public void cambiaStato(){
        ticketService.controlloScadenze();
        List<Ticket> tickets = ticketService.findAvanzabili(List.of("APERTO", "IN_LAVORAZIONE", "IN_ATTESA", "RISOLTO"));
        for(Ticket t : tickets){
            if(deveAvanzare(t)){
                ticketService.cambiaStato(t.getIdTicket());
                log.debug("Ticket " + t.getIdTicket() + " avanzato");
            }
        }
    }

    private boolean deveAvanzare(Ticket t){
        LocalDateTime ultimaModifica = ultimaModifica(t);
        if(ultimaModifica == null) return false;
        long oreTrascorse = Duration
                .between(ultimaModifica, LocalDateTime.now())
                .toHours();
        return oreTrascorse >= sogliaOre(t.getPriorita());
    }

    private long sogliaOre(Integer priorita){
        return switch(priorita){
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 8;
            default -> 12;
        };
    }

    private LocalDateTime ultimaModifica(Ticket t){
        return storicoStatoService
                .findUltimaTransizione(t.getIdTicket())
                .map(StoricoStato::getData_ora)
                .orElse(t.getData_ora_apertura());
    }
}