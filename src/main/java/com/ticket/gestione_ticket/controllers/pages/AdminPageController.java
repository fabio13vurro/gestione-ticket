package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.DTOs.TicketStatisticheDTO;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.TicketService;
import com.ticket.gestione_ticket.services.TicketStatisticheService;
import com.ticket.gestione_ticket.services.UtenteService;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPageController {

    private final UtenteService utenteService;
    private final TicketService ticketService;
    private final TicketStatisticheService statisticheService;

    @GetMapping("/utenti/crea")
    public String utentiCreaPage() { return "admin/utenti_crea"; }

    @PostMapping("/utenti/crea")
    public String utentiCreaSubmit(@RequestParam String username, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String ruolo,
                                   @RequestParam String via, @RequestParam String citta,
                                   Model model) {
        model.addAttribute("createResult", utenteService.create(username, email, password, ruolo, via, citta));
        return "redirect:/admin/utenti";
    }

    @GetMapping("/utenti")
    public String utentiLista(Model model, @RequestParam(defaultValue = "0") int pag, @RequestParam(required = false) String username,
                              @RequestParam(required = false) String email, @RequestParam(required = false) String ruolo,
                              @RequestParam(required = false) String ticketAssegnati, @RequestParam(required = false) String address) {
        username = pulisci(username);
        email = pulisci(email);
        ruolo = pulisci(ruolo);
        ticketAssegnati = pulisci(ticketAssegnati);
        address = pulisci(address);

        boolean filtroAttivo = utenteService.filtroAttivo(username, email, ruolo, ticketAssegnati, address);

        Page<Utente> utenti;
        if (filtroAttivo) {
            utenti = utenteService.filtraUtenti(username, email, ruolo, ticketAssegnati, address, PageRequest.of(pag, 20));
        }else {
            utenti = utenteService.getAll(PageRequest.of(pag, 20));
        }

        model.addAttribute("utenti", utenti);
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("ruolo", ruolo);
        model.addAttribute("ticketAssegnati", ticketAssegnati);
        model.addAttribute("address", address);
        model.addAttribute("filtroAttivo", filtroAttivo);
        return "admin/utenti_lista";
    }

    @GetMapping("/utenti/cerca")
    public String utentiCercaPage() { return "admin/utenti_cerca"; }

    @PostMapping("/utenti/cerca/id")
    public String cercaById(@RequestParam Integer id, Model model) {
        model.addAttribute("byId", utenteService.findById(id));
        return "admin/utenti_cerca";
    }

    @PostMapping("/utenti/cerca/username")
    public String cercaByUsername(@RequestParam String username, Model model) {
        model.addAttribute(("usernameSelezionato"), username);
        model.addAttribute("byUsername", utenteService.findByUsername(username));
        return "admin/utenti_cerca";
    }

    @PostMapping("/utenti/cerca/ruolo")
    public String cercaByRuolo(@RequestParam String ruolo, Model model) {
        model.addAttribute(("ruoloSelezionato"), ruolo);
        model.addAttribute("utentiByRuolo", utenteService.findByRuolo(Ruolo.valueOf(ruolo)));
        return "admin/utenti_cerca";
    }

    @GetMapping("/utenti/cancella")
    public String utentiCancellaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("utente", utenteService.findById(id));
        return "admin/utenti_cancella"; }

    @PostMapping("/utenti/cancella")
    public String utentiCancellaSubmit(@RequestParam Integer id) {
        utenteService.deleteById(id);
        return "redirect:/admin/utenti";
    }

    @GetMapping("/utenti/ripristina")
    public String utentiRipristinaPage(@RequestParam Integer id, Model model) {
        model.addAttribute("utente", utenteService.findById(id));
        return "admin/utenti_ripristina"; }

    @PostMapping("/utenti/ripristina")
    public String utentiRipristinaSubmit(@RequestParam Integer id) {
        utenteService.restoreDeletedColumn(id);
        return "redirect:/admin/utenti";
    }

    @GetMapping("/utenti/modifica")
    public String utentiModificaPage(@RequestParam Integer id, Model model){
        model.addAttribute("utente", utenteService.findById(id));
        return "admin/utenti_modifica";
    }

    @PostMapping("/utenti/modifica")
    public String utentiModificaSubmit(@RequestParam Integer id,
                                       @RequestParam(required = false) String username,
                                       @RequestParam(required = false) String email,
                                       @RequestParam(required = false) String password,
                                       @RequestParam(required = false) String via,
                                       @RequestParam(required = false) String citta,
                                       Model model) {
        model.addAttribute("updateResult", utenteService.update(id, username, email, password, via, citta));
        return "redirect:/admin/utenti";
    }

    @GetMapping("/ticket-scaduti")
    public String ticketScaduti(Model model, @RequestParam(defaultValue = "0") int pag){
        model.addAttribute("scaduti", ticketService.ticketScaduti(PageRequest.of(pag, 20)));
        return "admin/ticket_scaduti";
    }

    @GetMapping("/statistiche")
    public String statistiche(Model model){
        TicketStatisticheDTO dto = statisticheService.getStatistiche();
        model.addAttribute("statisticheAnno", dto.getStatistichePerAnno());
        model.addAttribute("statisticheOperatori", dto.getStatisticheOperatori());
        return "admin/statistiche";
    }

    private String pulisci(String val){
        return (val != null && !val.trim().isEmpty()) ? val.trim() : null;
    }
}