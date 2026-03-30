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
            "(:titolo IS NULL OR LOWER(t.titolo) LIKE LOWER(CONCAT('%', :titolo, '%'))) AND " +
            "(:descrizione IS NULL OR LOWER(t.descrizione) LIKE LOWER(CONCAT('%', :descrizione, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(t.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
            "(:stato IS NULL OR LOWER(t.stato) LIKE LOWER(CONCAT('%', :stato, '%'))) AND " +
            "(:priorita IS NULL OR CAST(t.priorita AS string) = :priorita) AND " +
            "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))")
    Page<Ticket> filtraTicket(@Param("titolo") String titolo,
                              @Param("descrizione") String descrizione,
                              @Param("categoria") String categoria,
                              @Param("stato") String stato,
                              @Param("priorita") String priorita,
                              @Param("username") String username,
                              Pageable pageable);
}