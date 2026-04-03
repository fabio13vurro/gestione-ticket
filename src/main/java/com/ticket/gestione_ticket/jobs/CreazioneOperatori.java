package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.services.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreazioneOperatori {
    private final UtenteService  utenteService;

    //@Scheduled(fixedDelayString = "${scheduler.fixedrate}")
    public void creazioneOperatori(){
        for (int i = 0; i<20; i++){
            utenteService.creazioneOperatore();
        }
    }
}