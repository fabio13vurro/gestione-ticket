package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.entities.Storico_Stato;
import com.ticket.gestione_ticket.services.Storico_StatoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storico_stato")
public class Storico_StatoController {

    private Storico_StatoService storico_statoService;

    public Storico_StatoController(Storico_StatoService storico_statoService) {
        this.storico_statoService = storico_statoService;
    }

    @PostMapping("/create")
    public Storico_Stato create(@RequestBody Storico_Stato storico_stato) {
        return storico_statoService.create(storico_stato);
    }

    @PutMapping("/update/{id}")
    public Storico_Stato update(@PathVariable int id, @RequestBody Storico_Stato storico_stato) {
        return storico_statoService.update(id, storico_stato);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable int id) {
        storico_statoService.deleteById(id);
    }

    @GetMapping
    public List<Storico_Stato> findAll() {
        return storico_statoService.findAll();
    }

    @GetMapping("/{id}")
    public Storico_Stato findById(@PathVariable int id) {
        return storico_statoService.findById(id);
    }
}