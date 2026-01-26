package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Tipo;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/cliente")
@PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
public class ClientePageController {

    private final TicketService ticketService;
    private final CommentoService commentoService;

    public ClientePageController(TicketService ticketService,
                                 CommentoService commentoService) {
        this.ticketService = ticketService;
        this.commentoService = commentoService;
    }

    @GetMapping("/ticket/crea")
    public String creaTicketPage() { return "cliente/ticket_crea"; }

    @PostMapping("/ticket/crea")
    public String creaTicketSubmit(@RequestParam String titolo, @RequestParam String descrizione,
                                   @RequestParam String categoria, @RequestParam Integer priorita,
                                   @RequestParam LocalDateTime data_ora_chiusura,
                                   @RequestParam Integer sla,
                                   Model model) {

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
        model.addAttribute("result", ticketService.create(t));
        model.addAttribute("success", "Ticket creato con successo.");
        return "cliente/ticket_crea";
    }

    @GetMapping("/ticket/cerca")
    public String cercaTicketPage() { return "cliente/ticket_cerca"; }

    @PostMapping("/ticket/cerca")
    public String cercaTicketSubmit(@RequestParam Integer id, Model model) {
        model.addAttribute("result", ticketService.findById(id));
        return "cliente/ticket_cerca";
    }

    @GetMapping("/commenti")
    public String commentiPage() { return "cliente/commenti"; }

    @PostMapping("/commenti/create")
    public String createCommento(@RequestParam String testo,
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
        model.addAttribute("createMsg", "Commento creato");
        return "cliente/commenti";
    }

    @PostMapping("/commenti/update")
    public String updateCommento(@RequestParam Integer id,
                                 @RequestParam(required=false) String testo,
                                 @RequestParam(required=false) String tipo,
                                 Model model) {
        Commento patch = new Commento();
        patch.setTesto(testo);
        if (tipo != null && !tipo.isBlank()) patch.setTipo(Tipo.valueOf(tipo));
        patch.setData_ora(LocalDateTime.now());
        model.addAttribute("updateResult", commentoService.update(id, patch));
        model.addAttribute("updateMsg", "Commento aggiornato");
        return "cliente/commenti";
    }

    @PostMapping("/commenti/findById")
    public String findCommentoById(@RequestParam Integer id, Model model) {
        model.addAttribute("findResult", commentoService.findById(id));
        return "cliente/commenti";
    }

    @PostMapping("/commenti/findByTipo")
    public String findCommentoByTipo(@RequestParam String tipo, Model model) {
        model.addAttribute("findByTipoResult", commentoService.findByTipo(Tipo.valueOf(tipo)));
        return "cliente/commenti";
    }
}