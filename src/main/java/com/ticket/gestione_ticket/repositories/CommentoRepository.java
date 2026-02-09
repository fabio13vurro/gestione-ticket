package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Tipo;
import org.springframework.stereotype.Repository;
import com.ticket.gestione_ticket.entities.Commento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Integer>{
    List<Commento> findByTipo(Tipo tipo);

}