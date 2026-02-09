package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Tipo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.TicketService;
import com.ticket.gestione_ticket.services.UtenteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

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
                                   @RequestParam Integer sla,
                                   Model model, Principal principal) {

        Ticket t = new Ticket();
        t.setTitolo(titolo);
        t.setDescrizione(descrizione);
        t.setCategoria(categoria);
        t.setPriorita(priorita);
        t.setStato("APERTO");
        t.setData_ora_apertura(LocalDateTime.now());
        t.setData_ora_chiusura(data_ora_chiusura);
        t.setSla(sla);
        t.setOver_sla(false);
        t.setDeleted(false);
        t.setCreated("interno");

        Utente u = utenteService.findByUsername(principal.getName());
        t.setUtente(u);

        model.addAttribute("result", ticketService.create(t));
        model.addAttribute("success", "Ticket creato con successo.");
        return "cliente/ticket_crea";
    }

    @GetMapping("/ticket/cerca")
    public String cercaTicketPage() { return "cliente/ticket_cerca"; }

    @PostMapping("/ticket/cerca")
    public String cercaTicketSubmit(@RequestParam Integer id, Model model) {
        model.addAttribute("ticket", ticketService.findById(id));
        return "cliente/ticket_cerca";
    }

    @GetMapping("/commenti")
    public String commentiPage(Model model) {
        model.addAttribute("commenti", commentoService.findAll());
        return "cliente/commenti"; }

    @GetMapping("/commenti/crea")
    public String creaCommentoPage() { return "cliente/commenti_crea"; }

    @PostMapping("/commenti/crea")
    public String creaCommentoSubmit(@RequestParam String testo,
                                 @RequestParam String tipo,
                                 @RequestParam(required=false) Integer ticketId,
                                 Model model) {
        Commento c = new Commento();
        c.setTesto(testo);
        c.setTipo(Tipo.valueOf(tipo));
        c.setData_ora(LocalDateTime.now());
        if (ticketId != null) {
            Ticket t = ticketService.findById(ticketId);
            c.setTicket(t);
        }
        model.addAttribute("createResult", commentoService.create(c));
        return "cliente/commenti";
    }

    @GetMapping("/commenti/modifica")
    public String modificaCommentoPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "cliente/commenti_modifica";
    }

    @PostMapping("/commenti/modifica")
    public String modificaCommentoSubmit(@RequestParam Integer id,
                                 @RequestParam(required=false) String testo,
                                 @RequestParam(required=false) String tipo,
                                 Model model) {
        Commento c = new Commento();
        if (testo !=null) c.setTesto(testo);
        if (tipo != null && !tipo.isBlank()) c.setTipo(Tipo.valueOf(tipo));
        c.setData_ora(LocalDateTime.now());
        model.addAttribute("updateResult", commentoService.update(id, c));
        return "cliente/commenti";
    }

    @GetMapping("/commenti/cancella")
    public String commentiCancellaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "cliente/commenti_cancella";
    }

    @PostMapping("/commenti/cancella")
    public String commentiCancellaSubmit(@RequestParam Integer id) {
        commentoService.deleteById(id);
        return "redirect:/cliente/commenti";
    }

    @GetMapping("/commenti/ripristina")
    public String commentiRipristinaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "cliente/commenti_ripristina";
    }

    @PostMapping("/commenti/ripristina")
    public String commentiRipristinaSubmit(@RequestParam Integer id) {
        commentoService.ripristina(id);
        return "redirect:/cliente/commenti";
    }

    @GetMapping("/commenti/cerca")
    public String cercaCommentiPage() { return "cliente/commenti_cerca"; }

    @PostMapping("/commenti/cerca/id")
    public String cercaById(@RequestParam Integer id, Model model) {
        model.addAttribute("byId", commentoService.findById(id));
        return "cliente/commenti_cerca";
    }

    @PostMapping("/commenti/cerca/tipo")
    public String findCommentoByTipo(@RequestParam String tipo, Model model) {
        model.addAttribute(("tipoSelezionato"), tipo);
        model.addAttribute("commentiByTipo", commentoService.findByTipo(Tipo.valueOf(tipo)));
        return "cliente/commenti_cerca";
    }
}