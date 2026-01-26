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
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCommento;

    private String testo;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private LocalDateTime data_ora;
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "codTicket")
    private Ticket ticket;
}
