package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.UtenteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPageController {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;

    public AdminPageController(UtenteService utenteService,  PasswordEncoder passwordEncoder) {
        this.utenteService = utenteService;
        this.passwordEncoder = new BCryptPasswordEncoder();
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
        u.setPassword(passwordEncoder.encode(password));
        u.setRuolo(Ruolo.valueOf(ruolo));
        u.setLibero(true);
        u.setDeleted(false);

        model.addAttribute("createResult", utenteService.create(u));
        model.addAttribute("success", "Utente creato");
        return "admin/utenti";
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

        var u = new Utente();
        if(username != null && !username.isBlank()) u.setUsername(username);
        if(email != null && !email.isBlank()) u.setEmail(email);
        if(password != null && !password.isBlank()) u.setPassword(passwordEncoder.encode(password));
        if(libero != null) u.setLibero(libero);
        model.addAttribute("updateResult", utenteService.update(id, u));
        model.addAttribute("success", "Utente modificato");

        return "redirect:/admin/utenti";
    }
}