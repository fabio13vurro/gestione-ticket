package com.ticket.gestione_ticket.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "storico_stato")
public class StoricoStato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_storico_stato")
    private Integer idStoricoStato;

    private String stato_precedente;
    private String stato_nuovo;
    private LocalDateTime data_ora;
    private Boolean deleted;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_ticket")
    private Ticket ticket;
}