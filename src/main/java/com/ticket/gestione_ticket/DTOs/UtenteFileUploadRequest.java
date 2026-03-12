package com.ticket.gestione_ticket.DTOs;

import lombok.Data;

@Data
public class UtenteFileUploadRequest {

    private String username;
    private String fileBase64;
}