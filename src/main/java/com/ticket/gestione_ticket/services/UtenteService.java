package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente create(Utente utente) {
        return utenteRepository.save(utente);
    }

    public void deleteById(int id) {
        utenteRepository.deleteById(id);
    }

    @Transactional
    public Utente update(Integer id, Utente new_utente) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + id));

        if(new_utente.getUsername()!=null) utente.setUsername(new_utente.getUsername());
        if(new_utente.getEmail()!=null) utente.setEmail(new_utente.getEmail());
        if(new_utente.getPassword()!=null) utente.setPassword(new_utente.getPassword());
        if(new_utente.getRuolo()!=null) utente.setRuolo(new_utente.getRuolo());
        utente.setLibero(new_utente.isLibero());
        return utenteRepository.save(utente);
    }

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Utente findById(Integer id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));
    }

    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    public Utente findByRuolo(String ruolo){
        return utenteRepository.findByRuolo(ruolo);
    }
}