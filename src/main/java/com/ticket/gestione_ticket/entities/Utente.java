package com.ticket.gestione_ticket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUtente;

    private String username;
    private String email;
    private String password;
    private String ruolo;
    private boolean libero;

    @OneToMany(mappedBy = "utente")
    private List<Ticket> ticket;
}
