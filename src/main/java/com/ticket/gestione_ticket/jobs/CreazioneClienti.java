package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.services.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreazioneClienti {
    private final UtenteService utenteService;

    @Scheduled(fixedDelayString = "${scheduler.fixedrate}")
    public void creazioneClienti(){
        for (int i = 0; i<100; i++){
            utenteService.creazioneCliente();
        }
    }
}