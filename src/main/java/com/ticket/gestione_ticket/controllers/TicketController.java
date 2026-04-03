package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.DTOs.creates.TicketCreateDTO;
import com.ticket.gestione_ticket.DTOs.updates.TicketUpdateDTO;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.services.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/lista")
    public Page<Ticket> filtraTicket(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(required = false) String titolo,
            @RequestParam(required = false) String descrizione,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String stato,
            @RequestParam(required = false) String priorita,
            @RequestParam(required = false) String username,
            @RequestParam(required = false)LocalDateTime aperturaDa,
            @RequestParam(required = false)LocalDateTime aperturaA,
            @RequestParam(required = false)LocalDateTime chiusuraDa,
            @RequestParam(required = false)LocalDateTime chiusuraA,
            @RequestParam(required = false)Integer numCommenti,
            @RequestParam(required = false) String created
            ) {

        boolean filtroAttivo = ticketService.filtroAttivo(titolo, descrizione, categoria, stato, priorita, username, aperturaDa, aperturaA, chiusuraDa, chiusuraA, numCommenti, created);

        Pageable pageable = PageRequest.of(pag, 20);

        if(filtroAttivo) return ticketService.filtraTicket(titolo, descrizione, categoria, stato, priorita, username, aperturaDa, aperturaA, chiusuraDa, chiusuraA, numCommenti, created, pageable);
        return ticketService.getAll(pageable);
    }
}