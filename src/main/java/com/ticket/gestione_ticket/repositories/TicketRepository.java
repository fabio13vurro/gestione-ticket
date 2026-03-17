package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findByTitolo(String titolo);

    @Query("SELECT t FROM Ticket t WHERE t.over_sla = true")
    List<Ticket> findByOverSlaTrue();

    @Query("""
       SELECT DISTINCT t
       FROM Ticket t
       LEFT JOIN FETCH t.commenti
       LEFT JOIN FETCH t.storici
       """)
    List<Ticket> findAllWithCommentiAndStorico();

    List<Ticket> findByStato(String stato);

    List<Ticket> findByPriorita(int priorita);

    List<Ticket> findByCategoria(String categoria);

    @Query("SELECT t FROM Ticket t WHERE t.utente.username = :username")
    List<Ticket> findByUtente_Username(String username);
}