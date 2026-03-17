package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.StoricoStato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoricoStatoRepository extends JpaRepository<StoricoStato, Integer> {
    @Query("SELECT s FROM StoricoStato s WHERE s.ticket.idTicket = :ticketId order by s.data_ora asc")
    List<StoricoStato> findByTicketId(Integer ticketId);
}