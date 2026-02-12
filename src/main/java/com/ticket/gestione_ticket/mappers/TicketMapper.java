package com.ticket.gestione_ticket.mappers;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.mongodb.documents.OverSlaTicketDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final CommentoMapper commentoMapper;

    public OverSlaTicketDocument toMongoSnapshot(Ticket t, LocalDateTime escalTimeStamp, LocalDateTime ultimoAgg){
        OverSlaTicketDocument doc = new OverSlaTicketDocument();
        doc.setTicketId(t.getIdTicket());
        doc.setTitolo(t.getTitolo());
        doc.setDescrizione(t.getDescrizione());
        doc.setCategoria(t.getCategoria());
        doc.setPriorita(t.getPriorita());
        doc.setStato(t.getStato());
        doc.setUltimoAggiornamento(ultimoAgg);
        doc.setEscalationTimeStamp(escalTimeStamp);
        doc.setCommenti(t.getCommenti().stream().map(commentoMapper::toMongo).toList());
        return doc;
    }
}