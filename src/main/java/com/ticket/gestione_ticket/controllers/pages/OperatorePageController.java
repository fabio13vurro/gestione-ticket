package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Storico_Stato;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.services.Storico_StatoService;
import com.ticket.gestione_ticket.services.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/operatore")
@PreAuthorize("hasAnyRole('OPERATORE','ADMIN')")
public class OperatorePageController {

    private final TicketService ticketService;
    private final Storico_StatoService storicoService;

    public OperatorePageController(TicketService ticketService, Storico_StatoService storicoService) {
        this.ticketService = ticketService;
        this.storicoService = storicoService;
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "operatore/ticket_lista";
    }

    @PostMapping("/ticket/delete")
    public String deleteTicket(@RequestParam Integer id) {
        ticketService.deleteById(id);
        return "redirect:/operatore/ticket";
    }

    @GetMapping("/ticket/stato")
    public String cambiaStatoPage() { return "operatore/ticket_cambia_stato"; }

    @PostMapping("/ticket/stato")
    public String cambiaStatoSubmit(@RequestParam Integer ticketId, @RequestParam String statoPrecedente,
                                    @RequestParam String statoNuovo, Model model) {
        Storico_Stato s = new Storico_Stato();
        s.setStato_precedente(statoPrecedente);
        s.setStato_nuovo(statoNuovo);
        s.setData_ora(LocalDateTime.now());
        s.setDeleted(false);
        Ticket t = ticketService.findById(ticketId);
        s.setTicket(t);
        model.addAttribute("result", storicoService.create(s));
        model.addAttribute("success", "Stato cambiato e storico registrato");
        return "operatore/ticket_cambia_stato";
    }

    @GetMapping("/storico")
    public String storicoLista(Model model) {
        model.addAttribute("storico", storicoService.findAll());
        return "operatore/storico_lista";
    }
}