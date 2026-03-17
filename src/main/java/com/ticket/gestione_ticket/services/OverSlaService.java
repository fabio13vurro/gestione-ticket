package com.ticket.gestione_ticket.services;


import com.ticket.gestione_ticket.config.JobQueueConfig;
import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.jobs.JobProducer;
import com.ticket.gestione_ticket.mappers.TicketMapper;
import com.ticket.gestione_ticket.mongodb.documents.OverSlaTicketDocument;
import com.ticket.gestione_ticket.mongodb.repositories.OverSlaTicketRepository;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class OverSlaService {
    private final TicketRepository ticketRepository;
    private final OverSlaTicketRepository overSlaTicketRepository;
    private final TicketMapper ticketMapper;
    private final ObjectMapper objectMapper;
    private final JobProducer jobProducer;


    public void controlloOverSla(){
        LocalDateTime now = LocalDateTime.now();

        List<Ticket> tickets = ticketRepository.findAllWithCommentiAndStorico();

        for(Ticket t : tickets){

            LocalDateTime ultimoAgg = t.getData_ora_apertura();

            if(t.getStorici() != null && !t.getStorici().isEmpty()){
                LocalDateTime maxStorico = t.getStorici().stream()
                        .map(StoricoStato::getData_ora)
                        .filter(Objects::nonNull)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);
                if (maxStorico != null && (ultimoAgg == null || ultimoAgg.isAfter(maxStorico))) {
                    ultimoAgg = maxStorico;
                }
            }

            if (t.getCommenti() != null && !t.getCommenti().isEmpty()) {
                LocalDateTime maxCommento = t.getCommenti().stream()
                        .map(commento -> commento.getData_ora())
                        .filter(Objects::nonNull)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);
                if (maxCommento != null && (ultimoAgg == null || ultimoAgg.isAfter(maxCommento))) {
                    ultimoAgg = maxCommento;
                }
            }

            if(t.getOver_sla().equals(true)) {
                var doc = ticketMapper.toMongoSnapshot(t, now, ultimoAgg);
                salva(doc);
                continue;
            }

            if(ultimoAgg != null && ultimoAgg.isBefore(now.minusHours(48))){
                System.out.println("Ticket " + t.getIdTicket() + " è over SLA. Setting true.");
                t.setOver_sla(true);
                ticketRepository.save(t);

                var doc = ticketMapper.toMongoSnapshot(t, now, ultimoAgg);
                salva(doc);
            }
        }
    }


    private void salva(OverSlaTicketDocument doc) {

        try {
            overSlaTicketRepository.save(doc);
            System.out.println("Snapshot salvato su MongoDB");
        } catch (Exception ex) {
            System.out.println("Mongo giù → invio snapshot su RabbitMQ");
            try {
                String payload = objectMapper.writeValueAsString(doc);
                jobProducer.sendJob(JobQueueConfig.MONGO_QUEUE, payload);
            } catch (Exception jsonEx) {
                System.out.println("Errore nel serializzare il documento: " + jsonEx.getMessage());
            }
        }
    }
}