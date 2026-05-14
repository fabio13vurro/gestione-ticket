package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findByTitolo(String titolo);

    @Query("SELECT t FROM Ticket t WHERE t.over_sla = true")
    List<Ticket> findByOverSlaTrue();

    @Query("SELECT t FROM Ticket t WHERE t.over_sla = true")
    Page<Ticket> findByOverSlaTrue(Pageable pageable);

    @Query("""
            SELECT DISTINCT t
            FROM Ticket t
            LEFT JOIN FETCH t.commenti
            LEFT JOIN FETCH t.storici
            """)
    List<Ticket> findAllWithCommentiAndStorico();

    @Query("SELECT t FROM Ticket t WHERE t.created = :username")
    Page<Ticket> findByCreated(String username, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.commenti LEFT JOIN t.utente u WHERE " +
            "(:titolo IS NULL OR LOWER(t.titolo) LIKE LOWER(CONCAT('%', :titolo, '%'))) AND " +
            "(:descrizione IS NULL OR LOWER(t.descrizione) LIKE LOWER(CONCAT('%', :descrizione, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(t.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
            "(:stato IS NULL OR LOWER(t.stato) LIKE LOWER(CONCAT('%', :stato, '%'))) AND " +
            "(:priorita IS NULL OR CAST(t.priorita AS string) = :priorita) AND " +
            "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:dataAperturaDa IS NULL OR t.data_ora_apertura >= :dataAperturaDa) AND " +
            "(:dataAperturaA IS NULL OR t.data_ora_apertura <= :dataAperturaA) AND " +
            "(:dataChiusuraDa IS NULL OR t.data_ora_chiusura >= :dataChiusuraDa) AND " +
            "(:dataChiusuraA IS NULL OR t.data_ora_chiusura <= :dataChiusuraA) AND " +
            "(:numCommenti IS NULL OR (SELECT COUNT(c) FROM Commento c WHERE c.ticket = t) = :numCommenti) AND " +
            "(:created IS NULL OR LOWER(t.created) LIKE LOWER(CONCAT('%', :created, '%')))")
    Page<Ticket> filtraTicket(@Param("titolo") String titolo,
                              @Param("descrizione") String descrizione,
                              @Param("categoria") String categoria,
                              @Param("stato") String stato,
                              @Param("priorita") String priorita,
                              @Param("username") String username,
                              @Param("dataAperturaDa") LocalDateTime dataAperturaDa,
                              @Param("dataAperturaA") LocalDateTime dataAperturaA,
                              @Param("dataChiusuraDa") LocalDateTime dataChiusuraDa,
                              @Param("dataChiusuraA") LocalDateTime dataChiusuraA,
                              @Param("numCommenti") Integer numCommenti,
                              @Param("created") String created,
                              Pageable pageable);

    @Query("SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.commenti LEFT JOIN t.utente u WHERE " +
            "u.username = :operatoreExact AND " +
            "(:titolo IS NULL OR LOWER(t.titolo) LIKE LOWER(CONCAT('%', :titolo, '%'))) AND " +
            "(:descrizione IS NULL OR LOWER(t.descrizione) LIKE LOWER(CONCAT('%', :descrizione, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(t.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
            "(:stato IS NULL OR LOWER(t.stato) LIKE LOWER(CONCAT('%', :stato, '%'))) AND " +
            "(:priorita IS NULL OR CAST(t.priorita AS string) = :priorita) AND " +
            "(:dataAperturaDa IS NULL OR t.data_ora_apertura >= :dataAperturaDa) AND " +
            "(:dataAperturaA IS NULL OR t.data_ora_apertura <= :dataAperturaA) AND " +
            "(:dataChiusuraDa IS NULL OR t.data_ora_chiusura >= :dataChiusuraDa) AND " +
            "(:dataChiusuraA IS NULL OR t.data_ora_chiusura <= :dataChiusuraA) AND " +
            "(:numCommenti IS NULL OR (SELECT COUNT(c) FROM Commento c WHERE c.ticket = t) = :numCommenti) AND " +
            "(:created IS NULL OR LOWER(t.created) LIKE LOWER(CONCAT('%', :created, '%')))")
    Page<Ticket> filtraTicketMiei(@Param("operatoreExact") String operatoreExact,
                                  @Param("titolo") String titolo,
                                  @Param("descrizione") String descrizione,
                                  @Param("categoria") String categoria,
                                  @Param("stato") String stato,
                                  @Param("priorita") String priorita,
                                  @Param("dataAperturaDa") LocalDateTime dataAperturaDa,
                                  @Param("dataAperturaA") LocalDateTime dataAperturaA,
                                  @Param("dataChiusuraDa") LocalDateTime dataChiusuraDa,
                                  @Param("dataChiusuraA") LocalDateTime dataChiusuraA,
                                  @Param("numCommenti") Integer numCommenti,
                                  @Param("created") String created,
                                  Pageable pageable);

    @Query("SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.commenti")
    Page<Ticket> findAllWithCommenti(Pageable pageable);

    Page<Ticket> findByUtente(Utente utente, Pageable pageable);
    List<Ticket> findByUtenteAndStatoNot(Utente utente, String stato);

    @Query("select t from Ticket t where t.stato in :stati and t.deleted = false")
    List<Ticket> findAvanzabili(@Param("stati") List<String> stati);

    @Query("""
        SELECT DISTINCT t FROM Ticket t
        LEFT JOIN FETCH t.commenti
        LEFT JOIN FETCH t.utente
        WHERE t.deleted = false
          AND t.stato = 'CHIUSO'
          AND t.data_ora_chiusura IS NOT NULL
          AND (
            YEAR(t.data_ora_chiusura) <> YEAR(t.data_ora_apertura)
            OR YEAR(t.data_ora_chiusura) <= :annoSoglia
          )
    """)
    List<Ticket> findTicketDaArchiviareConCommenti(@Param("annoSoglia") int annoSoglia);

    @Query("""
        SELECT DISTINCT t FROM Ticket t
        LEFT JOIN FETCH t.storici
        WHERE t.deleted = false
          AND t.stato = 'CHIUSO'
          AND t.data_ora_chiusura IS NOT NULL
          AND (
            YEAR(t.data_ora_chiusura) <> YEAR(t.data_ora_apertura)
            OR YEAR(t.data_ora_chiusura) <= :annoSoglia
          )
    """)
    List<Ticket> findTicketDaArchiviareConStorici(@Param("annoSoglia") int annoSoglia);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.data_ora_apertura BETWEEN :start AND :end")
    long countTicketPerAnno(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.data_ora_apertura BETWEEN :start AND :end AND t.stato = :stato")
    long countTicketChiusiPerAnno(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("stato") String stato);

    @Query("SELECT COUNT(DISTINCT t.utente.idUtente) FROM Ticket t WHERE t.stato = :stato")
    long operatoriAttivi(@Param("stato") String stato);

    @Query("SELECT COUNT(DISTINCT t.utente.idUtente) FROM Ticket t")
    long totaleOperatori();

    @Query("SELECT t.stato, COUNT(t) FROM Ticket t WHERE t.utente.username = :username GROUP BY t.stato")
    List<Object[]> ticketOperatorePerStato(@Param("username") String username);
}