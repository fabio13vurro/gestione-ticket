package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.*;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.TicketService;
import com.ticket.gestione_ticket.services.UtenteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasAnyRole('CLIENTE', 'OPERATORE', 'ADMIN')")
public class ClientePageController {

    private final TicketService ticketService;
    private final CommentoService commentoService;
    private final UtenteService utenteService;

    public ClientePageController(TicketService ticketService,CommentoService commentoService, UtenteService utenteService) {
        this.ticketService = ticketService;
        this.commentoService = commentoService;
        this.utenteService = utenteService;
    }

    @GetMapping("/ticket/crea")
    public String creaTicketPage() { return "cliente/ticket_crea"; }

    @PostMapping("/ticket/crea")
    public String creaTicketSubmit(@RequestParam String titolo, @RequestParam String descrizione,
                                   @RequestParam String categoria, @RequestParam Integer priorita,
                                   @RequestParam LocalDateTime data_ora_chiusura,
                                   Model model, Principal principal) {

        Ticket t = new Ticket();
        t.setTitolo(titolo);
        t.setDescrizione(descrizione);
        t.setCategoria(categoria);
        t.setPriorita(priorita);
        t.setStato("APERTO");
        t.setData_ora_apertura(LocalDateTime.now());
        t.setData_ora_chiusura(data_ora_chiusura);
        t.setSla(2);
        t.setOver_sla(false);
        t.setDeleted(false);
        t.setCreated("interno");

        Utente u = utenteService.findByUsername(principal.getName());
        t.setUtente(u);

        model.addAttribute("result", ticketService.create(t));
        model.addAttribute("success", "Ticket creato con successo.");
        return "cliente/ticket_crea";
    }

    @GetMapping("/commenti/crea")
    public String creaCommentoPage(@RequestParam Integer ticketId, Model model) {
        model.addAttribute("ticketSelezionato", ticketService.findById(ticketId));
        return "cliente/commenti_crea";
    }

    @PostMapping("/commenti/crea")
    public String creaCommentoSubmit(@RequestParam String testo,
                                 @RequestParam String tipo,
                                 @RequestParam Integer ticketId,
                                 Model model, Principal principal) {
        Ticket t = ticketService.findById(ticketId);

        Commento c = new Commento();
        c.setTesto(testo);
        Utente u = utenteService.findByUsername(principal.getName());
        if (u.getRuolo() == Ruolo.CLIENTE) {
            c.setTipo(Tipo.ESTERNO);
        } else {
            c.setTipo(Tipo.valueOf(tipo));
        }
        c.setTicket(t);
        c.setData_ora(LocalDateTime.now());
        c.setDeleted(false);
        commentoService.create(c);
        model.addAttribute("ticketSelezionato", t);
        model.addAttribute("success", "Commento creato con successo.");
        return "cliente/commenti_crea";
    }

    @GetMapping("/miei-ticket")
    public String mieiTicketPage(Model model, Principal principal) {
        String username = principal.getName();
        List<Ticket> t = ticketService.findByUtente_Username(username);
        model.addAttribute("mieiTicket", t);
        return "cliente/miei_ticket";
    }

    @GetMapping("/miei-commenti")
    public String mieiCommenti(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("mieiCommenti",
                commentoService.findByTicket_Utente_Username(username));
        return "cliente/miei_commenti";
    }
}