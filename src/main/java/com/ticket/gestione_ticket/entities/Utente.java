package com.ticket.gestione_ticket.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUtente;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @Column(name = "ticket_assegnati")
    private Integer ticketAssegnati;
    private Boolean deleted;
    private String address;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fileData;

    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>();
}
