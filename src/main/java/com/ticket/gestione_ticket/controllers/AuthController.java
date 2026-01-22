
package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.dto.LoginRequest;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import com.ticket.gestione_ticket.auth.TokenStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UtenteRepository utenteRepository;
    private final TokenStore tokenStore;

    public AuthController(UtenteRepository utenteRepository, TokenStore tokenStore) {
        this.utenteRepository = utenteRepository;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        Utente utente = utenteRepository.findByUsername(request.getUsername());

        if (utente == null) return "Username non valido";

        if (!utente.getPassword().equals(request.getPassword())) return "Password errata";

        String token = tokenStore.generateToken(utente.getUsername());
        return token;
    }
}
