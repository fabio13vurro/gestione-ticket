package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.mappers.TicketMapper;
import com.ticket.gestione_ticket.mongodb.repositories.OverSlaTicketRepository;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OverSlaService {
    private final TicketRepository ticketRepository;
    private final OverSlaTicketRepository overSlaTicketRepository;
    private final TicketMapper ticketMapper;

    public OverSlaService(TicketRepository ticketRepository, OverSlaTicketRepository overSlaTicketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.overSlaTicketRepository = overSlaTicketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public void controlloOverSla(){
        LocalDateTime now = LocalDateTime.now();

        List<Ticket> tickets = ticketRepository.findAll();

        for(Ticket t : tickets){

            LocalDateTime ultimoAgg;

            if(t.getStorico_stato() != null && t.getStorico_stato().getData_ora() != null){
                ultimoAgg = t.getStorico_stato().getData_ora();
            }else{
                ultimoAgg = t.getData_ora_apertura();
            }

            if(t.getOver_sla().equals(true)) {
                if(!overSlaTicketRepository.existsByTicketId(t.getIdTicket())){
                    var doc = ticketMapper.toMongoSnapshot(t, now, ultimoAgg);
                    overSlaTicketRepository.save(doc);
                }
                continue;
            }

            if(ultimoAgg != null && ultimoAgg.isBefore(now.minusHours(48))){
                t.setOver_sla(true);
                ticketRepository.save(t);

                var doc = ticketMapper.toMongoSnapshot(t, now, ultimoAgg);
                overSlaTicketRepository.save(doc);
            }
        }
    }
}