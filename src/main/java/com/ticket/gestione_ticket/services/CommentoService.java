package com.ticket.gestione_ticket.services;

import com.mongodb.lang.Nullable;
import com.ticket.gestione_ticket.entities.*;
import com.ticket.gestione_ticket.repositories.CommentoRepository;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;
    private final TicketRepository ticketRepository;
    private final UtenteRepository utenteRepository;
    private final Faker faker = new Faker(new Locale("it"));

    public Commento create(String testo, String tipo, Integer ticketId, String username){
        Ticket t = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket non trovato: " + ticketId));
        Utente u = utenteRepository.findByUsername(username);
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
        c.setCreated(username);
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

    public Page<Commento> findByCreated(String username, Pageable pageable){
        return commentoRepository.findByCreated(username, pageable);
    }

    public Page<Commento> findByTicketIdOrderByDataOraAsc(Integer ticketId, Pageable pageable){
        return commentoRepository.findByTicketIdOrderByDataOraAsc(ticketId, pageable);
    }

    public void creazioneCommenti(Ticket ticket) {
        List<Utente> clienti = utenteRepository.findByRuolo(Ruolo.CLIENTE);
        List<Utente> operatori = utenteRepository.findByRuolo(Ruolo.OPERATORE);
        String[] tipiOperatore = {"INTERNO", "ESTERNO"};
        int numCommenti = faker.number().numberBetween(2, 11);

        for (int i = 0; i < numCommenti; i++) {
            boolean isCliente = faker.bool().bool();
            Utente autore = isCliente
                    ? clienti.get(faker.number().numberBetween(0, clienti.size()))
                    : operatori.get(faker.number().numberBetween(0, operatori.size()));

            String testo = faker.lorem().paragraph(1);
            String tipo = isCliente ? null : tipiOperatore[faker.number().numberBetween(0, 2)];

            create(testo, tipo, ticket.getIdTicket(), autore.getUsername());
        }
    }

    public Page<Commento> getAll(Pageable pageable){
        return commentoRepository.findAll(pageable);
    }

    public Page<Commento> filtraCommenti(String testo, String tipo, String codTicket, String created, LocalDateTime dataOraDa, LocalDateTime dataOraA, Pageable pageable){
        return commentoRepository.filtraCommenti(testo, tipo, codTicket, created, dataOraDa, dataOraA, pageable);
    }

    public boolean filtroAttivo(String testo, String tipo, String codTicket, String created, LocalDateTime dataOraDa, LocalDateTime dataOraA){
        return testo != null || tipo != null || codTicket != null || created != null || dataOraDa != null || dataOraA != null;
    }
}