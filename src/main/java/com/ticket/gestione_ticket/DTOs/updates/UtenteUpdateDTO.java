package com.ticket.gestione_ticket.DTOs.updates;

public record UtenteUpdateDTO(
        String username,
        String email,
        String password,
        String via,
        String citta
) {}