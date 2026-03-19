package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasAnyRole('CLIENTE', 'OPERATORE', 'ADMIN')")
public class ClientePageController {

    private final TicketService ticketService;
    private final CommentoService commentoService;

    @GetMapping("/ticket/crea")
    public String creaTicketPage() { return "cliente/ticket_crea"; }

    @PostMapping("/ticket/crea")
    public String creaTicketSubmit(@RequestParam String titolo, @RequestParam String descrizione,
                                   @RequestParam String categoria, @RequestParam Integer priorita,
                                   Model model, Principal principal) {
        model.addAttribute("result",ticketService.create(titolo, descrizione, categoria, priorita, principal.getName()));
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
        Commento c = commentoService.create(testo, tipo, ticketId, principal.getName());
        model.addAttribute("ticketSelezionato", c.getTicket());
        model.addAttribute("success", "Commento creato con successo.");
        return "cliente/commenti_crea";
    }

    @GetMapping("/miei-ticket")
    public String mieiTicketPage(Model model, Principal principal) {
        model.addAttribute("mieiTicket", ticketService.findByUtente_Username(principal.getName()));
        return "cliente/miei_ticket";
    }

    @GetMapping("/miei-commenti")
    public String mieiCommenti(Model model, Principal principal) {
        model.addAttribute("mieiCommenti", commentoService.findByTicket_Utente_Username(principal.getName()));
        return "cliente/miei_commenti";
    }
}