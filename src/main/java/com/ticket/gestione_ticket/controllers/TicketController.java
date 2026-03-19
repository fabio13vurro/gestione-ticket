package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.services.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/cliente/create")
    public Ticket create(String titolo, String descr, String categoria, Integer priorita, String username) {
        return ticketService.create(titolo, descr, categoria, priorita, username);
    }

    @PutMapping("/cliente/update/{id}")
    public Ticket update(@PathVariable int id, String titolo, String descr, String categoria, Integer priorita) {
        return ticketService.update(id, titolo, descr, categoria, priorita);
    }

    @DeleteMapping("/operatore/delete/{id}")
    public void deleteById(@PathVariable int id) {
        ticketService.deleteById(id);
    }

    @GetMapping("operatore/findAll")
    public List<Ticket> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("operatore/find/{id}")
    public Ticket findById(@PathVariable int id) {
        return ticketService.findById(id);
    }

    @GetMapping("/operatore/find/{titolo}")
    public Ticket findByTitolo(@PathVariable String titolo){
        return ticketService.findByTitolo(titolo);
    }
}
