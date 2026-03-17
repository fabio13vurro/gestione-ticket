package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.entities.Storico_Stato;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Tipo;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.Storico_StatoService;
import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/operatore")
@PreAuthorize("hasAnyRole('OPERATORE','ADMIN')")
public class OperatorePageController {

    private final TicketService ticketService;
    private final Storico_StatoService storicoService;
    private final CommentoService commentoService;

    @GetMapping("/ticket")
    public String listaTicket(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "operatore/ticket_lista";
    }

    @GetMapping("/ticket/commenti")
    public String listaCommenti(@RequestParam Integer id, Model model) {
        model.addAttribute("ticketId", id);
        model.addAttribute("commenti", commentoService.findByTicketIdOrderByData_oraAsc(id));
        return "operatore/ticket_commenti";
    }

    @GetMapping("/ticket/cerca")
    public String cercaTicketPage() { return "operatore/ticket_cerca"; }

    @PostMapping("/ticket/cerca/id")
    public String cercaById(@RequestParam Integer id, Model model) {
        model.addAttribute("byId", ticketService.findById(id));
        return "operatore/ticket_cerca";
    }

    @PostMapping("/ticket/cerca/stato")
    public String cercaByStato(@RequestParam String stato, Model model) {
        model.addAttribute("ticketsByStato", ticketService.findByStato(stato));
        return "operatore/ticket_cerca";
    }

    @PostMapping("/ticket/cerca/categoria")
    public String cercaByCategoria(@RequestParam String categoria, Model model) {
        model.addAttribute("ticketsByCategoria", ticketService.findByCategoria(categoria));
        return "operatore/ticket_cerca";
    }

    @PostMapping("/ticket/cerca/priorita")
    public String cercaByPriorita(@RequestParam Integer priorita, Model model) {
        model.addAttribute("ticketsByPriorita", ticketService.findByPriorita(priorita));
        return "operatore/ticket_cerca";
    }

    @PostMapping("/ticket/cerca/utente")
    public String cercaByUtente(@RequestParam String username, Model model) {
        model.addAttribute("ticketsByUtente", ticketService.findByUtente_Username(username));
        return "operatore/ticket_cerca";
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

    @GetMapping("/commenti")
    public String commentiPage(Model model) {
        model.addAttribute("commenti", commentoService.findAll());
        return "operatore/commenti"; }

    @GetMapping("/commenti/modifica")
    public String modificaCommentoPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "operatore/commenti_modifica";
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
        return "operatore/commenti";
    }

    @GetMapping("/commenti/cancella")
    public String commentiCancellaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "operatore/commenti_cancella";
    }

    @PostMapping("/commenti/cancella")
    public String commentiCancellaSubmit(@RequestParam Integer id) {
        commentoService.deleteById(id);
        return "redirect:/operatore/commenti";
    }

    @GetMapping("/commenti/ripristina")
    public String commentiRipristinaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "operatore/commenti_ripristina";
    }

    @PostMapping("/commenti/ripristina")
    public String commentiRipristinaSubmit(@RequestParam Integer id) {
        commentoService.ripristina(id);
        return "redirect:/operatore/commenti";
    }

    @GetMapping("/commenti/cerca")
    public String cercaCommentiPage() { return "operatore/commenti_cerca"; }

    @PostMapping("/commenti/cerca/id")
    public String cercaCommentiById(@RequestParam Integer id, Model model) {
        model.addAttribute("byId", commentoService.findById(id));
        return "operatore/commenti_cerca";
    }

    @PostMapping("/commenti/cerca/tipo")
    public String findCommentoByTipo(@RequestParam String tipo, Model model) {
        model.addAttribute(("tipoSelezionato"), tipo);
        model.addAttribute("commentiByTipo", commentoService.findByTipo(Tipo.valueOf(tipo)));
        return "operatore/commenti_cerca";
    }
}