package com.ticket.gestione_ticket.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCommento;

    private String testo;
    private String tipo;
    private LocalDateTime data_ora;

    @ManyToOne
    @JoinColumn(name = "codTicket")
    private Ticket ticket;
}
