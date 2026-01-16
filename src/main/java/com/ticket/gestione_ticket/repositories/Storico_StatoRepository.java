package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Storico_Stato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Storico_StatoRepository extends JpaRepository<Storico_Stato, Integer> {
}