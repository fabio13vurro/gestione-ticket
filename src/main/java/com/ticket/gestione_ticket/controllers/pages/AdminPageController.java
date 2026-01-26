package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.UtenteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPageController {

    private final UtenteService utenteService;

    public AdminPageController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/utenti/crea")
    public String utentiCreaPage() { return "admin/utenti_crea"; }

    @PostMapping("/utenti/crea")
    public String utentiCreaSubmit(@RequestParam String username, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String ruolo,
                                   Model model) {
        Utente u = new Utente();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        u.setRuolo(Ruolo.valueOf(ruolo));
        u.setLibero(true);
        u.setDeleted(false);
        model.addAttribute("createResult", utenteService.create(u));
        model.addAttribute("success", "Utente creato");

        return "admin/utenti_crea";
    }

    @GetMapping("/utenti")
    public String utentiLista(Model model) {
        model.addAttribute("utenti", utenteService.findAll());
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
        model.addAttribute("byUsername", utenteService.findByUsername(username));
        return "admin/utenti_cerca";
    }

    @PostMapping("/utenti/cerca/ruolo")
    public String cercaByRuolo(@RequestParam String ruolo, Model model) {
        model.addAttribute("byRuolo", utenteService.findByRuolo(Ruolo.valueOf(ruolo)));
        return "admin/utenti_cerca";
    }

    @GetMapping("/utenti/cancella")
    public String utentiCancellaPage() { return "admin/utenti_cancella"; }

    @PostMapping("/utenti/cancella")
    public String utentiCancellaSubmit(@RequestParam Integer id) {
        utenteService.deleteById(id);
        return "redirect:/admin/utenti";
    }

}