package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket create(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteById(int id) {
        ticketRepository.deleteById(id);
    }


    @Transactional
    public Ticket update(Integer id, Ticket new_ticket) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));

        if (new_ticket.getTitolo() != null) ticket.setTitolo(new_ticket.getTitolo());
        if (new_ticket.getDescrizione() != null) ticket.setDescrizione(new_ticket.getDescrizione());
        if (new_ticket.getCategoria() != null) ticket.setCategoria(new_ticket.getCategoria());
        if (new_ticket.getPriorita() != null) ticket.setPriorita(new_ticket.getPriorita());
        if (new_ticket.getStato() != null) ticket.setStato(new_ticket.getStato());
        if (new_ticket.getData_ora_apertura() != null) ticket.setData_ora_apertura(new_ticket.getData_ora_apertura());
        if (new_ticket.getData_ora_chiusura() != null) ticket.setData_ora_chiusura(new_ticket.getData_ora_chiusura());
        if (new_ticket.getSla() != null) ticket.setSla(new_ticket.getSla());
        if (new_ticket.getOver_sla() != null) ticket.setOver_sla(new_ticket.getOver_sla());

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
}