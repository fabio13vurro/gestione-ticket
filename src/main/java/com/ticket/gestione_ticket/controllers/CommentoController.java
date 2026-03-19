package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.DTOs.creates.CommentoCreateDTO;
import com.ticket.gestione_ticket.DTOs.updates.CommentoUpdateDTO;
import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.entities.Tipo;
import com.ticket.gestione_ticket.services.CommentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commenti")
public class CommentoController {

    private CommentoService commentoService;

    public CommentoController(CommentoService commentoService) {
        this.commentoService = commentoService;
    }

    @PostMapping("/cliente/create")
    public Commento create(@RequestBody CommentoCreateDTO dto) {
        return commentoService.create(dto.testo(), dto.tipo(), dto.ticketId(), dto.username());
    }

    @PutMapping("/cliente/update/{id}")
    public Commento update(@PathVariable int id, @RequestBody CommentoUpdateDTO dto) {
        return commentoService.update(id, dto.testo());
    }

    @DeleteMapping("/admin/delete/{id}")
    public void deleteById(@PathVariable int id) {
        commentoService.deleteById(id);
    }

    @GetMapping
    public List<Commento> findAll() {
        return commentoService.findAll();
    }

    @GetMapping("/cliente/find/{id}")
    public Commento findById(@PathVariable int id) {
        return commentoService.findById(id);
    }

    @GetMapping("/cliente/find/tipo/{tipo}")
    public List<Commento> findByTipo(@RequestParam Tipo tipo){
        return commentoService.findByTipo(tipo);
    }
}