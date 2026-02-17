package com.ticket.gestione_ticket.mongodb.repositories;

import com.ticket.gestione_ticket.mongodb.documents.OverSlaTicketDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OverSlaTicketRepository extends MongoRepository<OverSlaTicketDocument, String> {}