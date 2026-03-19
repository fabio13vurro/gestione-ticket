package com.ticket.gestione_ticket.services;

import com.mongodb.lang.Nullable;
import com.ticket.gestione_ticket.entities.*;
import com.ticket.gestione_ticket.repositories.CommentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;
    private final TicketService ticketService;
    private final UtenteService utenteService;

    public Commento create(String testo, String tipo, Integer ticketId, String username){
        Ticket t = ticketService.findById(ticketId);
        Utente u = utenteService.findByUsername(username);
        Commento c = new Commento();

        c.setTesto(testo);
        if (u.getRuolo() == Ruolo.CLIENTE) {
            c.setTipo(Tipo.ESTERNO);
        } else {
            c.setTipo(Tipo.valueOf(tipo));
        }
        c.setTicket(t);
        c.setData_ora(LocalDateTime.now());
        c.setDeleted(false);

        return commentoRepository.save(c);
    }

    public void deleteById(int id){
        Commento commento = commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato: " + id));

        commento.setDeleted(true);
        commentoRepository.save(commento);
    }

    public void ripristina(Integer id){
        Commento commento = commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato: " + id));

        commento.setDeleted(false);
        commentoRepository.save(commento);
    }

    @Transactional
    public Commento update(Integer id, @Nullable String testo){
        Commento commento = findById(id);

        boolean modifica = false;

        if(testo != null && !testo.isBlank() && !testo.equals(commento.getTesto())) {
            commento.setTesto(testo);
            commento.setData_ora(LocalDateTime.now());
            modifica = true;
        }

        if(!modifica) return commento;

        return commentoRepository.save(commento);
    }

    public List<Commento> findAll(){
        return commentoRepository.findAll();
    }

    public Commento findById(Integer id){
        return commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato: " + id));
    }

    public List<Commento> findByTipo(Tipo tipo){
        return commentoRepository.findByTipo(tipo);
    }

    public List<Commento> findByTicket_Utente_Username(String username){
        return commentoRepository.findByTicket_Utente_Username(username);
    }

    public List<Commento> findByTicketIdOrderByData_oraAsc(Integer ticketId){
        return commentoRepository.findByTicketIdOrderByData_oraAsc(ticketId);
    }
}