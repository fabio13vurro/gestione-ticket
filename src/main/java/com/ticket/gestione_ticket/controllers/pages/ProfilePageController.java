package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.DTOs.UtenteFileUploadRequest;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profilo")
public class ProfilePageController {

    private final UtenteService utenteService;

    @GetMapping
    public String profiloPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente utente = utenteService.findByUsername(username);
        model.addAttribute("utente", utente);
        return "profilo";
    }

    @PostMapping("/aggiorna-immagine")
    public String aggiornaImmagine(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!file.isEmpty()) {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            UtenteFileUploadRequest req = new UtenteFileUploadRequest();
            req.setUsername(username);
            req.setFileBase64(base64);
            utenteService.aggiungiFileBase64(req);
        }
        return "redirect:/profilo";
    }

    @PostMapping("/rimuovi-immagine")
    public String rimuoviImmagine() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        utenteService.rimuoviImmagine(username);
        return "redirect:/profilo";
    }
}
