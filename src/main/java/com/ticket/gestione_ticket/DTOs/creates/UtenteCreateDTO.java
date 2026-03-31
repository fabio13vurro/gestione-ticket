package com.ticket.gestione_ticket.DTOs.creates;

public record UtenteCreateDTO(
        String username,
        String email,
        String password,
        String ruolo,
        String via,
        String citta
) {}