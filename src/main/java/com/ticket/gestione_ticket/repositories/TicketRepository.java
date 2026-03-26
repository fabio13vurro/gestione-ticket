package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT t FROM Ticket t LEFT JOIN t.utente u WHERE " +
            "LOWER(t.titolo) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.descrizione) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.categoria) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.stato) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "(u IS NOT NULL AND LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))) OR " +
            "CAST(t.data_ora_apertura AS string) LIKE CONCAT('%', :q, '%') OR " +
            "CAST(t.data_ora_chiusura AS string) LIKE CONCAT('%', :q, '%')")
    Page<Ticket> cercaTicket(@Param("q") String q, Pageable pageable);
}