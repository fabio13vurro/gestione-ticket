package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.StoricoStato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoricoStatoRepository extends JpaRepository<StoricoStato, Integer> {

}