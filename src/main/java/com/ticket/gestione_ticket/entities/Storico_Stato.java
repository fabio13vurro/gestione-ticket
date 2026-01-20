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
    private Integer idStorico_Stato;

    private String stato_precedente;
    private String stato_nuovo;
    private LocalDateTime data_ora;

    @OneToOne
    @JoinColumn(name = "cod_ticket")
    private Ticket ticket;
}