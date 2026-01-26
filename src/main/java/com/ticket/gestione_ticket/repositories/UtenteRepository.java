package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Utente findByUsername(String username);

    List<Utente> findByRuolo(Ruolo ruolo);
}
