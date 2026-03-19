package com.ticket.gestione_ticket.DTOs.creates;

public record CommentoCreateDTO(
        String testo,
        String tipo,
        Integer ticketId,
        String username
) {}