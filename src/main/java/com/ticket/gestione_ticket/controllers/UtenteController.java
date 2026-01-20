package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.UtenteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/create")
    public Utente create(@RequestBody Utente utente) {
        return utenteService.create(utente);
    }

    @PutMapping("/update/{id}")
    public Utente update(@PathVariable int id, @RequestBody Utente utente) {
        return utenteService.update(id, utente);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable int id) {
        utenteService.deleteById(id);
    }

    @GetMapping
    public List<Utente> findAll() {
        return utenteService.findAll();
    }

    @GetMapping("/{id}")
    public Utente findById(@PathVariable int id) {
        return utenteService.findById(id);
    }

    @GetMapping("/find/username/{username}")
    public Utente findByUsername(@PathVariable String username){
        return utenteService.findByUsername(username);
    }

    @GetMapping("/find/ruolo/{ruolo}")
    public Utente findByRuolo(@PathVariable Ruolo ruolo){
        return utenteService.findByRuolo(ruolo);
    }
}