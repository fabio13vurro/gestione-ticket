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

    @DeleteMapping("/admin/delete/{id}")
    public void deleteById(@PathVariable int id) {
        utenteService.deleteById(id);
    }

    @GetMapping("/admin/findALl")
    public List<Utente> findAll() {
        return utenteService.findAll();
    }

    @GetMapping("/admin/find/{id}")
    public Utente findById(@PathVariable int id) {
        return utenteService.findById(id);
    }

    @GetMapping("/admin/find/username/{username}")
    public Utente findByUsername(@PathVariable String username){
        return utenteService.findByUsername(username);
    }

    @GetMapping("/admin/find/ruolo/{ruolo}")
    public List<Utente> findByRuolo(@PathVariable Ruolo ruolo){
        return utenteService.findByRuolo(ruolo);
    }
}