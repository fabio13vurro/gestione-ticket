package com.ticket.gestione_ticket.services;

import com.mongodb.lang.Nullable;
import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.jobs.JobProducer;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StoricoStatoService storicoService;
    private final UtenteService utenteService;

    public Ticket create(String titolo, String descr, String categoria, Integer priorita, String username) {
        Ticket t = new Ticket();
        t.setTitolo(titolo);
        t.setDescrizione(descr);
        t.setCategoria(categoria);
        t.setPriorita(priorita);
        t.setStato("APERTO");
        t.setData_ora_apertura(LocalDateTime.now());
        t.setSla(2);
        t.setOver_sla(false);
        t.setCreated("interno");

        Utente u = utenteService.findByUsername(username);
        t.setUtente(u);

        return ticketRepository.save(t);
    }

    public void deleteById(int id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));

        ticket.setDeleted(true);
        ticketRepository.save(ticket);
    }

    public void ripristina(Integer id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));

        ticket.setDeleted(false);
        ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket update(Integer id, @Nullable String titolo, @Nullable String descrizione, @Nullable String categoria,
                        @Nullable Integer priorita, @Nullable Integer sla) {
        Ticket ticket = findById(id);

        boolean modifica = false;

        if (titolo != null && !titolo.isBlank() && !titolo.equals(ticket.getTitolo())) {
            ticket.setTitolo(titolo);
            modifica = true;
        }

        if (descrizione != null && !descrizione.isBlank() && !descrizione.equals(ticket.getDescrizione())) {
            ticket.setDescrizione(descrizione);
            modifica = true;
        }

        if (categoria != null && !categoria.isBlank() && !categoria.equals(ticket.getCategoria())) {
            ticket.setCategoria(categoria);
            modifica = true;
        }

        if (priorita != null && !priorita.equals(ticket.getPriorita())
                && priorita > 0 && priorita < 5) {
            ticket.setPriorita(priorita);
            modifica = true;
        }

        if (sla != null && !sla.equals(ticket.getSla())
                && sla > 0) {
            ticket.setSla(sla);
            modifica = true;
        }

        if(!modifica) return ticket;

        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket findById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));
    }

    public Ticket findByTitolo(String titolo){
        return ticketRepository.findByTitolo(titolo);
    }

    public List<Ticket> findByStato(String stato){
        return ticketRepository.findByStato(stato);
    }

    public List<Ticket> findByPriorita(int priorita){
        return ticketRepository.findByPriorita(priorita);
    }

    public List<Ticket> findByCategoria(String categoria){
        return ticketRepository.findByCategoria(categoria);
    }

    public List<Ticket> findByUtente_Username(String username){
        return ticketRepository.findByUtente_Username(username);
    }

    public Ticket cambiaStato(Integer id){
        Ticket t = findById(id);
        if(t == null) return null;

        String statoNuovo, statoAttuale = t.getStato();

        if(statoAttuale.equals("CHIUSO")) return t;

        switch (statoAttuale) {
            case "APERTO":
                statoNuovo = "IN_LAVORAZIONE";
                break;
            case "IN_LAVORAZIONE":
                statoNuovo = "IN_ATTESA";
                break;
            case "IN_ATTESA":
                statoNuovo = "RISOLTO";
                break;
            case "RISOLTO":
                statoNuovo = "CHIUSO";
                t.setData_ora_chiusura(LocalDateTime.now());
                break;
            default:
                statoNuovo = statoAttuale;
        }

        storicoService.create(t, statoAttuale, statoNuovo);
        t.setStato(statoNuovo);
        return ticketRepository.save(t);
    }

    public Ticket creazioneTicket(String titolo, String descrizione) {
        Ticket t = new Ticket();
        t.setTitolo(titolo);
        t.setDescrizione(descrizione);
        t.setCategoria("MONITORAGGIO");
        t.setPriorita(3);
        t.setStato("APERTO");
        t.setData_ora_apertura(LocalDateTime.now());
        t.setOver_sla(false);
        t.setDeleted(false);
        t.setSla(2);
        t.setCreated("esterno");

        return ticketRepository.save(t);
    }

}