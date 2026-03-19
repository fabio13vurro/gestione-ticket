package com.ticket.gestione_ticket.DTOs.updates;

public record TicketUpdateDTO(
        String titolo,
        String descr,
        String categoria,
        Integer priorita
) {}