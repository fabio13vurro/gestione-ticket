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

    @GetMapping("/ticket/cancella")
    public String ticketCancellaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("ticket", ticketService.findById(id));
        return "operatore/ticket_cancella";
    }

    @PostMapping("/ticket/cancella")
    public String ticketCancellaSubmit(@RequestParam Integer id) {
        ticketService.deleteById(id);
        return "redirect:/operatore/ticket";
    }

    @GetMapping("/ticket/ripristina")
    public String ticketRipristinaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("ticket", ticketService.findById(id));
        return "operatore/ticket_ripristina";
    }

    @PostMapping("/ticket/ripristina")
    public String ticketRipristinaSubmit(@RequestParam Integer id) {
        ticketService.ripristina(id);
        return "redirect:/operatore/ticket";
    }

    @GetMapping("/ticket/modifica")
    public String ticketModificaPage(@RequestParam Integer id, Model model){
        model.addAttribute("ticket", ticketService.findById(id));
        return "operatore/ticket_modifica";
    }

    @PostMapping("/ticket/modifica")
    public String ticketModificaSubmit(@RequestParam Integer id, @RequestParam(required = false) String titolo,
                                       @RequestParam(required = false) String descrizione, @RequestParam(required = false) String categoria,
                                       @RequestParam(required = false) Integer priorita, @RequestParam(required = false) Integer sla, Model model) {
        var t = new Ticket();
        if(titolo != null && !titolo.isBlank()) t.setTitolo(titolo);
        if(descrizione != null && !descrizione.isBlank())  t.setDescrizione(descrizione);
        if(categoria != null && !categoria.isBlank()) t.setCategoria(categoria);
        if(priorita != null && priorita != 0)  t.setPriorita(priorita);
        if(sla != null && sla != 0)  t.setSla(sla);
        model.addAttribute("updateResults", ticketService.update(id, t));
        model.addAttribute("success", "Utente modificato");
        return "redirect:/operatore/ticket";
    }

    @GetMapping("/ticket/stato")
    public String cambiaStatoPage() { return "operatore/ticket_cambia_stato"; }

    @PostMapping("/ticket/stato")
    public String cambiaStatoSubmit(@RequestParam Integer ticketId, @RequestParam String statoNuovo, Model model) {
        Storico_Stato s = new Storico_Stato();
        s.setStato_nuovo(statoNuovo);
        s.setData_ora(LocalDateTime.now());
        s.setDeleted(false);
        Ticket t = ticketService.findById(ticketId);
        s.setStato_precedente(t.getStato());
        t.setStato(statoNuovo);
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

    @GetMapping("/storico/cancella")
    public String cancellaStoricoPage(@RequestParam Integer id, Model model) {
        model.addAttribute("storico", storicoService.findById(id));
        return "operatore/storico_cancella";
    }

    @PostMapping("/storico/cancella")
    public String cancellaStoricoSubmit(@RequestParam Integer id) {
        storicoService.deleteById(id);
        return "redirect:/operatore/storico";
    }

    @GetMapping("/storico/ripristina")
    public String ripristinaStoricoPage(@RequestParam Integer id, Model model) {
        model.addAttribute("storico", storicoService.findById(id));
        return "operatore/storico_ripristina";
    }

    @PostMapping("/storico/ripristina")
    public String ripristinaStoricoSubmit(@RequestParam Integer id){
        storicoService.ripristina(id);
        return "redirect:/operatore/storico";
    }
}