package com.ticket.gestione_ticket.mongodb.documents;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ticket_over_sla")
public class OverSlaTicketDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private Integer ticketId;

    private String titolo;
    private String descrizione;
    private String categoria;
    private Integer priorita;
    private String stato;
    private LocalDateTime ultimoAggiornamento;
    private LocalDateTime escalationTimeStamp;

    private List<CommentoMongo> commenti;
}