package com.ticket.gestione_ticket.repositories;

import org.springframework.stereotype.Repository;
import com.ticket.gestione_ticket.entities.Commento;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Integer>{
    Commento findByTipo(String tipo);
}