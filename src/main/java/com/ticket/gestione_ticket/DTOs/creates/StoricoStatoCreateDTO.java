package com.ticket.gestione_ticket.DTOs.creates;

import com.ticket.gestione_ticket.entities.Ticket;

public record StoricoStatoCreateDTO(
        Ticket ticket,
        String statoAttuale,
        String statoNuovo
) {}