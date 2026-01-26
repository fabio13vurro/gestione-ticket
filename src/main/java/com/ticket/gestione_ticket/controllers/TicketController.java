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
    public Ticket create(@RequestBody Ticket ticket) {
        return ticketService.create(ticket);
    }

    @PutMapping("/cliente/update/{id}")
    public Ticket update(@PathVariable int id, @RequestBody Ticket ticket) {
        return ticketService.update(id, ticket);
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
