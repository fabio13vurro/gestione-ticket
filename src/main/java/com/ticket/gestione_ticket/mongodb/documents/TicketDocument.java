package com.ticket.gestione_ticket.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private Integer idTicket;

    private String titolo;
    private String descrizione;
    private String categoria;
    private Integer priorita;
    private String stato;

    private LocalDateTime dataOraApertura;
    private LocalDateTime dataOraChiusura;
    private LocalDateTime dataOraScadenza;
    private Boolean overSla;
    private Integer annoApertura;
    private Integer annoChiusura;
    private LocalDateTime archiviatoIl;

    private UtenteSnapshot utente;
    private List<CommentoSnapshot> commenti;
    private List<StoricoStatoSnapshot> storicoStati;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UtenteSnapshot {
        private Integer idUtente;
        private String username;
        private String email;
        private String ruolo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentoSnapshot {
        private Integer idCommento;
        private String testo;
        private String tipo;
        private LocalDateTime dataOra;
        private String created;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoricoStatoSnapshot {
        private Integer idStoricoStato;
        private String statoPrecedente;
        private String statoNuovo;
        private LocalDateTime dataOra;
    }
}