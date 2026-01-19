package com.ticket.gestione_ticket.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Storico_Stato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStorico_Stato;

    private String stato_precedente;
    private String stato_nuovo;
    private LocalDateTime data_ora;

    @OneToOne(mappedBy = "storico_stato")
    private Ticket ticket;
}