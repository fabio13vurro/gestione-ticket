package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Tipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ticket.gestione_ticket.entities.Commento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Integer>{
    List<Commento> findByTipo(Tipo tipo);

    @Query("SELECT c FROM Commento c WHERE c.created = :username")
    Page<Commento> findByCreated(String username, Pageable pageable);

    @Query("SELECT c FROM Commento c WHERE c.ticket.idTicket = :ticketId ORDER BY c.data_ora ASC")
    Page<Commento> findByTicketIdOrderByDataOraAsc(@Param("ticketId") Integer ticketId, Pageable pageable);

    @Query("SELECT c FROM Commento c WHERE " +
            "(:testo IS NULL OR LOWER(c.testo) LIKE LOWER(CONCAT('%', :testo, '%'))) AND " +
            "(:tipo IS NULL OR LOWER(c.tipo) = LOWER(:tipo)) AND " +
            "(:codTicket IS NULL OR CAST(c.ticket.idTicket AS string) = :codTicket) AND " +
            "(:created IS NULL OR LOWER(c.created) LIKE LOWER(CONCAT('%', :created, '%'))) AND " +
            "(:dataOraDa IS NULL OR c.data_ora >= :dataOraDa) AND " +
            "(:dataOraA IS NULL OR c.data_ora <= :dataOraA)")
    Page<Commento> filtraCommenti(@Param("testo") String testo,
                                  @Param("tipo") String tipo,
                                  @Param("codTicket") String codTicket,
                                  @Param("created") String created,
                                  @Param("dataOraDa") LocalDateTime dataOraDa,
                                  @Param("dataOraA") LocalDateTime dataOraA,
                                  Pageable pageable);
}