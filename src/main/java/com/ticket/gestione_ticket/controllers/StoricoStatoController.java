package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.DTOs.creates.StoricoStatoCreateDTO;
import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.services.StoricoStatoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storicoStato")
public class StoricoStatoController {

    private StoricoStatoService storicoStatoService;

    public StoricoStatoController(StoricoStatoService storicoStatoService) {
        this.storicoStatoService = storicoStatoService;
    }

    @PostMapping("/operatore/create")
    public StoricoStato create(@RequestBody StoricoStatoCreateDTO dto) {
        return storicoStatoService.create(dto.ticket(), dto.statoAttuale(), dto.statoNuovo());
    }

    @PutMapping("/operatore/update/{id}")
    public StoricoStato update(@PathVariable int id, @RequestBody StoricoStato storicoStato) {
        return storicoStatoService.update(id, storicoStato);
    }

    @DeleteMapping("/operatore/delete/{id}")
    public void deleteById(@PathVariable int id) {
        storicoStatoService.deleteById(id);
    }

    @GetMapping("/operatore/findAll")
    public List<StoricoStato> findAll() {
        return storicoStatoService.findAll();
    }

    @GetMapping("/operatore/find/{id}")
    public StoricoStato findById(@PathVariable int id) {
        return storicoStatoService.findById(id);
    }
}