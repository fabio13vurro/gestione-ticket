package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.services.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/utenti/crea")
    public String utentiCreaPage() { return "admin/utenti_crea"; }

    @PostMapping("/utenti/crea")
    public String utentiCreaSubmit(@RequestParam String username, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String ruolo,
                                   Model model) {
        model.addAttribute("createResult", utenteService.create(username, email, password, ruolo));
        model.addAttribute("success", "Utente creato");
        return "redirect:/admin/utenti";
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
                                       @RequestParam(required = false) Boolean libero,
                                       Model model) {
        model.addAttribute("updateResult", utenteService.update(id, username, email, password, libero));
        return "redirect:/admin/utenti";
    }
}