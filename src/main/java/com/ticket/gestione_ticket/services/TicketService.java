package com.ticket.gestione_ticket.services;

import com.mongodb.lang.Nullable;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        t.setOver_sla(false);
        t.setCreated(username);
        t.setDeleted(false);
        t.setData_ora_scadenza(calcolaScadenza(priorita));

        return ticketRepository.save(t);
    }

    public LocalDateTime calcolaScadenza(Integer priorita){
        LocalDateTime apertura = LocalDateTime.now();
        return switch (priorita){
            case 1 -> apertura.plusHours(12);
            case 2 -> apertura.plusDays(1);
            case 3 -> apertura.plusHours(36);
            default -> apertura.plusDays(2);
        };
    }

    public void controlloScadenze(){
        LocalDateTime now = LocalDateTime.now();
        List<Ticket> tickets = ticketRepository.findAll();
        for(Ticket t : tickets){
            if(t.getOver_sla().equals(true) || t.getStato().equals("CHIUSO") || t.getDeleted().equals(true) || t.getStato().equals("IN_ATTESA")) {
                continue;
            }else{
                if(t.getData_ora_scadenza() != null && now.isAfter(t.getData_ora_scadenza())){
                    t.setOver_sla(true);
                    t.setStato("SCADUTO");
                    ticketRepository.save(t);
                }
            }
        }
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
                        @Nullable Integer priorita) {
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

        if(!modifica) return ticket;

        return ticketRepository.save(ticket);
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
            case "SCADUTO":
                statoNuovo = "APERTO";
                t.setData_ora_scadenza(calcolaScadenza(t.getPriorita()));
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
        t.setCreated("esterno");

        return ticketRepository.save(t);
    }

    public Page<Ticket> getAll(Pageable pageable){
        return ticketRepository.findAll(pageable);
    }

    public Page<Ticket> filtraTicket(String titolo, String descrizione, String categoria, String stato, String priorita, String username, Pageable pageable){
        return ticketRepository.filtraTicket(titolo, descrizione, categoria, stato, priorita, username, pageable);
    }

    public boolean filtroAttivo(String titolo, String descrizione, String categoria, String stato, String priorita, String username){
        titolo      = (titolo      != null && !titolo.trim().isEmpty())      ? titolo.trim()      : null;
        descrizione = (descrizione != null && !descrizione.trim().isEmpty()) ? descrizione.trim() : null;
        categoria   = (categoria   != null && !categoria.trim().isEmpty())   ? categoria.trim()   : null;
        stato       = (stato       != null && !stato.trim().isEmpty())       ? stato.trim()       : null;
        priorita    = (priorita    != null && !priorita.trim().isEmpty())    ? priorita.trim()    : null;
        username    = (username    != null && !username.trim().isEmpty())    ? username.trim()    : null;

        return titolo != null || descrizione != null || categoria != null
                || stato != null || priorita != null || username != null;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> ticketScaduti(){
        return ticketRepository.findByOverSlaTrue();
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
}