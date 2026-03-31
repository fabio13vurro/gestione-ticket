package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Tipo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ticket.gestione_ticket.entities.Commento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Integer>{
    List<Commento> findByTipo(Tipo tipo);

    @Query("SELECT c FROM Commento c WHERE c.ticket.created = :username")
    List<Commento> findByTicketCreated(@Param("username") String username);

    @Query("SELECT c FROM Commento c WHERE c.ticket.idTicket = :ticketId order by c.data_ora asc")
    List<Commento> findByTicketIdOrderByData_oraAsc(@Param("ticketId") Integer ticketId);
}