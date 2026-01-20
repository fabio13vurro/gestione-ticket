package com.ticket.gestione_ticket.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"commenti", "storico_stato", "utente"})

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTicket;

    private String titolo;
    private String descrizione;
    private String categoria;
    private Integer priorita;
    private String stato;
    private LocalDateTime data_ora_apertura;
    private LocalDateTime data_ora_chiusura;
    private Integer sla;
    private Boolean over_sla;
    private Boolean deleted = false;

    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    private List<Commento> commenti = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cod_utente")
    private Utente utente;

    @OneToOne(mappedBy = "ticket")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Storico_Stato storico_stato;
}
