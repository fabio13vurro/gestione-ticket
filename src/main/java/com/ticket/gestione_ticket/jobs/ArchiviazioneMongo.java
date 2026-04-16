package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.services.ArchiviazioneTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArchiviazioneMongo {
    private final ArchiviazioneTicketService archiviazioneTicketService;

    @Scheduled(fixedDelayString = "${scheduler.check}")
    public void archiviazioneTicket(){
        archiviazioneTicketService.archiviaTutti();
    }
}