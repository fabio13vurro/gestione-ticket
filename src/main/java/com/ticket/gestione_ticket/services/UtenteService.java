package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.DTOs.UtenteFileUploadRequest;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public Utente create(String username, String email, String password, String ruolo) {
        Utente u = new Utente();

        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRuolo(Ruolo.valueOf(ruolo));
        u.setLibero(true);
        u.setDeleted(false);

        return utenteRepository.save(u);
    }

    public Utente aggiungiFileBase64(UtenteFileUploadRequest req){
        Utente u = utenteRepository.findByUsername(req.getUsername());
        if(u == null) throw new RuntimeException("Utente non trovato: " + req.getUsername());
        u.setFileData(req.getFileBase64());
        return utenteRepository.save(u);
    }

    public void deleteById(int id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + id));

        utente.setDeleted(true);
        utenteRepository.save(utente);
    }

    public void restoreDeletedColumn(Integer id){
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + id));

        utente.setDeleted(false);
        utenteRepository.save(utente);
    }

    @Transactional
    public Utente update(Integer id, String username, String email, String password, Boolean libero) {
        Utente utente = findById(id);
        boolean modifica = false;

        if(username != null && !username.isBlank() && !username.equals(utente.getUsername())) {
            utente.setUsername(username);
            modifica = true;
        }

        if(email != null && !email.isBlank() && !email.equals(utente.getEmail())) {
            utente.setEmail(email);
            modifica = true;
        }

        if(password != null && !password.isBlank() && !password.equals(utente.getPassword())) {
            modifica = true;
            utente.setPassword(passwordEncoder.encode(password));
        }

        if(libero != null && !libero.equals(utente.getLibero())) {
            modifica = true;
            utente.setLibero(libero);
        }

        if(!modifica) return utente;

        return utenteRepository.save(utente);
    }

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Utente findById(Integer id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + id));
    }

    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    public List<Utente> findByRuolo(Ruolo ruolo){
        return utenteRepository.findByRuolo(ruolo);
    }
}