package com.ticket.gestione_ticket.DTOs.creates;

public record TicketCreateDTO(
        String titolo,
        String descr,
        String categoria,
        Integer priorita,
        String username
) {}