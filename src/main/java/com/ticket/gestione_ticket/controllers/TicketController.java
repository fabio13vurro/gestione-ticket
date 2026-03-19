package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.DTOs.creates.TicketCreateDTO;
import com.ticket.gestione_ticket.DTOs.updates.TicketUpdateDTO;
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
    public Ticket create(@RequestBody TicketCreateDTO dto) {
        return ticketService.create(dto.titolo(), dto.descr(), dto.categoria(), dto.priorita(), dto.username());
    }

    @PutMapping("/cliente/update/{id}")
    public Ticket update(@PathVariable int id, TicketUpdateDTO dto) {
        return ticketService.update(id, dto.titolo(), dto.descr(), dto.categoria(), dto.priorita());
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
