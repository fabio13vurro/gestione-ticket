package com.ticket.gestione_ticket.controllers;

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

    @PostMapping("/create")
    public Commento create(@RequestBody Commento commento) {
        return commentoService.create(commento);
    }

    @PutMapping("/update/{id}")
    public Commento update(@PathVariable int id, @RequestBody Commento commento) {
        return commentoService.update(id, commento);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable int id) {
        commentoService.deleteById(id);
    }

    @GetMapping
    public List<Commento> findAll() {
        return commentoService.findAll();
    }

    @GetMapping("/{id}")
    public Commento findById(@PathVariable int id) {
        return commentoService.findById(id);
    }

    @GetMapping("/find/tipo/{tipo}")
    public Commento findByTipo(@RequestParam Tipo tipo){
        return commentoService.findByTipo(tipo);
    }
}