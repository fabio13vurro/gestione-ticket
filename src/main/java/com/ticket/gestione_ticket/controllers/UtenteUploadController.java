package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.DTOs.UtenteFileUploadRequest;
import com.ticket.gestione_ticket.services.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/utenti")
public class UtenteUploadController {
    private final UtenteService utenteService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestBody UtenteFileUploadRequest req) {
        utenteService.aggiungiFileBase64(req);
        return ResponseEntity.ok("File inserito");
    }
}