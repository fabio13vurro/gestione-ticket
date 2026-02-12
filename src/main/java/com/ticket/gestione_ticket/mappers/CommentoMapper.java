package com.ticket.gestione_ticket.mappers;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.mongodb.documents.CommentoMongo;
import org.springframework.stereotype.Component;

@Component
public class CommentoMapper {
    public CommentoMongo toMongo(Commento commento){
        if(commento==null) return null;
        return new CommentoMongo(
                commento.getTesto(),
                commento.getTipo().name(),
                commento.getData_ora()
        );
    }
}