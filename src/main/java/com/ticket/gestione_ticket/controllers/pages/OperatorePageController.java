package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Tipo;
import com.ticket.gestione_ticket.services.CommentoService;
import com.ticket.gestione_ticket.services.StoricoStatoService;
import com.ticket.gestione_ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/operatore")
@PreAuthorize("hasAnyRole('OPERATORE','ADMIN')")
public class OperatorePageController {

    private final TicketService ticketService;
    private final StoricoStatoService storicoService;
    private final CommentoService commentoService;

    @GetMapping("/ticket")
    public String listaTicket(Model model, @RequestParam(defaultValue = "0") int pag, @RequestParam(required = false) String titolo,
                              @RequestParam(required = false) String descrizione, @RequestParam(required = false) String categoria,
                              @RequestParam(required = false) String stato, @RequestParam(required = false) String priorita, @RequestParam(required = false) String username,
                              @RequestParam(required = false) String dataAperturaDa, @RequestParam(required = false) String dataAperturaA,
                              @RequestParam(required = false) String dataChiusuraDa, @RequestParam(required = false) String dataChiusuraA,
                              @RequestParam(required = false) Integer numCommenti, @RequestParam(required = false) String created) {

        titolo = pulisci(titolo);
        descrizione = pulisci(descrizione);
        categoria   = pulisci(categoria);
        stato       = pulisci(stato);
        priorita    = pulisci(priorita);
        username    = pulisci(username);
        created     = pulisci(created);

        LocalDateTime aperturaDa  = parseData(dataAperturaDa, false);
        LocalDateTime aperturaA   = parseData(dataAperturaA, true);
        LocalDateTime chiusuraDa  = parseData(dataChiusuraDa, false);
        LocalDateTime chiusuraA   = parseData(dataChiusuraA, true);

        boolean filtroAttivo = ticketService.filtroAttivo(titolo, descrizione, categoria, stato, priorita, username, aperturaDa, aperturaA, chiusuraDa, chiusuraA, numCommenti, created);

        Page<Ticket> tickets;
        if (filtroAttivo) {
            tickets = ticketService.filtraTicket(titolo, descrizione, categoria, stato, priorita, username, aperturaDa, aperturaA, chiusuraDa, chiusuraA, numCommenti, created, PageRequest.of(pag, 20));
        }else{
            tickets = ticketService.getAll(PageRequest.of(pag, 20));
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("titolo", titolo);
        model.addAttribute("descrizione", descrizione);
        model.addAttribute("categoria", categoria);
        model.addAttribute("stato", stato);
        model.addAttribute("priorita", priorita);
        model.addAttribute("username", username);
        model.addAttribute("dataAperturaDa", dataAperturaDa);
        model.addAttribute("dataAperturaA",  dataAperturaA);
        model.addAttribute("dataChiusuraDa", dataChiusuraDa);
        model.addAttribute("dataChiusuraA",  dataChiusuraA);
        model.addAttribute("numCommenti", numCommenti);
        model.addAttribute("created", created);
        model.addAttribute("filtroAttivo", filtroAttivo);
        return "operatore/ticket_lista";
    }

    @GetMapping("/ticket/commenti")
    public String listaCommenti(Model model, @RequestParam Integer id, @RequestParam(defaultValue = "0") int pag) {
        model.addAttribute("ticketId", id);
        model.addAttribute("commenti", commentoService.findByTicketIdOrderByDataOraAsc(id, PageRequest.of(pag, 20)));
        return "operatore/ticket_commenti";
    }

    @GetMapping("/ticket/cerca")
    public String cercaTicketPage() { return "operatore/ticket_cerca"; }

    @GetMapping("/ticket/cerca/id")
    public String cercaById(@RequestParam Integer id, Model model) {
        model.addAttribute("idSelezionato", id);
        model.addAttribute("byId", ticketService.findById(id));
        return "operatore/ticket_cerca";
    }

    @GetMapping("/ticket/cerca/stato")
    public String cercaByStato(@RequestParam String stato, Model model) {
        model.addAttribute("statoSelezionato", stato);
        model.addAttribute("ticketsByStato", ticketService.findByStato(stato));
        return "operatore/ticket_cerca";
    }

    @GetMapping("/ticket/cerca/categoria")
    public String cercaByCategoria(@RequestParam String categoria, Model model) {
        model.addAttribute("categoriaSelezionata", categoria);
        model.addAttribute("ticketsByCategoria", ticketService.findByCategoria(categoria));
        return "operatore/ticket_cerca";
    }

    @GetMapping("/ticket/cerca/priorita")
    public String cercaByPriorita(@RequestParam Integer priorita, Model model) {
        model.addAttribute("prioritaSelezionata", priorita);
        model.addAttribute("ticketsByPriorita", ticketService.findByPriorita(priorita));
        return "operatore/ticket_cerca";
    }

    @GetMapping("/ticket/cerca/utente")
    public String cercaByUtente(@RequestParam String username, Model model) {
        model.addAttribute("usernameSelezionato", username);
        model.addAttribute("ticketsByUtente", ticketService.findByCreated(username));
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
                                       @RequestParam(required = false) Integer priorita, @RequestParam(required = false) Model model) {
        model.addAttribute("updateResults", ticketService.update(id, titolo, descrizione, categoria, priorita));
        model.addAttribute("success", "Ticket modificato");
        return "redirect:/operatore/ticket";
    }

    @GetMapping("/ticket/stato")
    public String cambiaStato(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        Ticket t = ticketService.findById(id);
        ticketService.cambiaStato(id);
        redirectAttributes.addFlashAttribute("success", "Stato del ticket modificato con successo.");
        return "redirect:/operatore/ticket";
    }

    @GetMapping("/ticket/storico")
    public String storicoLista(@RequestParam Integer id, Model model) {
        model.addAttribute("ticketId", id);
        model.addAttribute("storico", storicoService.findByTicketId(id));
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
    public String commentiPage(Model model, @RequestParam(defaultValue = "0") int pag,
                               @RequestParam(required = false) String testo, @RequestParam(required = false) String tipo,
                               @RequestParam(required = false) String codTicket, @RequestParam(required = false) String created,
                               @RequestParam(required = false) String dataOraDa, @RequestParam(required = false) String dataOraA) {
        testo = pulisci(testo);
        tipo = pulisci(tipo);
        codTicket = pulisci(codTicket);
        created = pulisci(created);

        LocalDateTime dataDa =  parseData(dataOraDa, false);
        LocalDateTime dataA =  parseData(dataOraA, true);

        boolean filtroAttivo = commentoService.filtroAttivo(testo, tipo, codTicket, created, dataDa, dataA);

        Page<Commento> commenti;
        if (filtroAttivo) {
            commenti = commentoService.filtraCommenti(testo, tipo, codTicket, created, dataDa, dataA, PageRequest.of(pag, 20));
        }else{
            commenti = commentoService.getAll(PageRequest.of(pag, 20));
        }

        model.addAttribute("commenti", commenti);
        model.addAttribute("testo", testo);
        model.addAttribute("tipo", tipo);
        model.addAttribute("codTicket", codTicket);
        model.addAttribute("created", created);
        model.addAttribute("dataOraDa", dataOraDa);
        model.addAttribute("dataOraA", dataOraA);
        model.addAttribute("filtroAttivo", filtroAttivo);
        return "operatore/commenti"; }

    @GetMapping("/commenti/modifica")
    public String modificaCommentoPage(@RequestParam Integer id, Model model) {
        model.addAttribute("commento", commentoService.findById(id));
        return "operatore/commenti_modifica";
    }

    @PostMapping("/commenti/modifica")
    public String modificaCommentoSubmit(@RequestParam Integer id,
                                         @RequestParam(required=false) String testo,
                                         Model model) {
        model.addAttribute("updateResult", commentoService.update(id, testo));
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

    private String pulisci(String val) {
        return (val != null && !val.trim().isEmpty()) ? val.trim() : null;
    }

    private LocalDateTime parseData(String data, boolean fineGiornata) {
        if (data == null || data.trim().isEmpty()) return null;
        try {
            LocalDate d = LocalDate.parse(data.trim());
            return fineGiornata ? d.atTime(23, 59, 59) : d.atStartOfDay();
        } catch (Exception e) {
            return null;
        }
    }
}