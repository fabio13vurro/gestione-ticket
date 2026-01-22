
package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.auth.TokenStore;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access")
@SecurityRequirement(name = "bearerAuth")

public class AccessController {

    private final TokenStore tokenStore;
    private final UtenteRepository utenteRepository;

    public AccessController(TokenStore tokenStore, UtenteRepository utenteRepository) {
        this.tokenStore = tokenStore;
        this.utenteRepository = utenteRepository;
    }

    @GetMapping("/admin/test")
    public String adminTest(@RequestHeader("Authorization") String authHeader) {

        if (!authHeader.startsWith("Bearer ")) return "Token mancante";

        String token = authHeader.substring(7);

        if (!tokenStore.isValidToken(token)) return "Token non valido";

        String username = tokenStore.getUsernameFromToken(token);
        Utente utente = utenteRepository.findByUsername(username);

        if(utente == null) return "Utente non trovato";

        if(utente.getRuolo()!= Ruolo.ADMIN) return "Accesso negato";

        return "Accesso admin riuscito";
    }

    @GetMapping("/operatore/test")
    public String operatoreTest(@RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) return "Token mancante";

        String token = authHeader.substring(7);

        if(!tokenStore.isValidToken(token)) return "Token non valido";

        String username = tokenStore.getUsernameFromToken(token);
        Utente utente = utenteRepository.findByUsername(username);

        if(utente == null) return "Utente non trovato";

        if(utente.getRuolo()== Ruolo.CLIENTE) return "Accesso negato";

        return "Accesso operatore riuscito da " + utente.getRuolo();
    }

    @GetMapping("/cliente/test")
    public String clienteTest(@RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) return "Token mancante";

        String token = authHeader.substring(7);

        if(!tokenStore.isValidToken(token)) return "Token non valido";

        String username = tokenStore.getUsernameFromToken(token);
        Utente utente = utenteRepository.findByUsername(username);

        if(utente == null) return "Utente non trovato";

        if(utente.getRuolo() == Ruolo.OPERATORE) return "Accesso negato";

        return "Accesso cliente riuscito da " + utente.getRuolo();
    }
}
