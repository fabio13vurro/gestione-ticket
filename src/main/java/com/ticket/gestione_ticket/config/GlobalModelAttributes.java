package com.ticket.gestione_ticket.config;

import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final UtenteService utenteService;

    /**
     * Cattura l'URL corrente inclusi i parametri di ricerca.
     * Utile per i redirect che devono mantenere lo stato (paginazione, filtri).
     */
    @ModelAttribute("currentUrl")
    public String currentUrl(HttpServletRequest request) {
        String query = request.getQueryString();
        return request.getRequestURI() + (query != null ? "?" + query : "");
    }

    /**
     * Aggiunge l'utente corrente al model di ogni pagina Thymeleaf.
     * Usato principalmente per mostrare l'avatar nel navbar.
     */
    @ModelAttribute("currentUtente")
    public Utente currentUtente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(String.valueOf(auth.getPrincipal()))) {
            try {
                return utenteService.findByUsername(auth.getName());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
