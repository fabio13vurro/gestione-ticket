package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.DTOs.UtenteFileUploadRequest;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public Utente create(String username, String email, String password, String ruolo) {
        Utente u = new Utente();

        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRuolo(Ruolo.valueOf(ruolo));
        u.setTicketAssegnati(0);
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
        Utente utente = findById(id);

        if (utente.getRuolo() == Ruolo.OPERATORE) {
            List<Ticket> tickets = ticketRepository.findByUtenteAndStatoNot(utente, "CHIUSO");

            for(Ticket t : tickets){
                Utente operatore = utenteRepository.findByRuolo(Ruolo.OPERATORE)
                        .stream()
                        .filter(u -> !u.getDeleted() && !u.getIdUtente().equals(id))
                        .min(Comparator.comparing(Utente::getTicketAssegnati))
                        .orElseThrow(() -> new RuntimeException("Nessun operatore disponibile"));

                t.setUtente(operatore);
                operatore.setTicketAssegnati(operatore.getTicketAssegnati() + 1);
                utenteRepository.save(operatore);
                ticketRepository.save(t);
            }
        }

        utente.setTicketAssegnati(0);
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
    public Utente update(Integer id, String username, String email, String password) {
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

    public Utente save(Utente u){
        return utenteRepository.save(u);
    }
}