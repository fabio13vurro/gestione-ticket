package com.ticket.gestione_ticket.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentoMongo {
    private String testo;
    private String tipo;
    private LocalDateTime dataOra;
}