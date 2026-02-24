package com.ticket.gestione_ticket.controllers.pages;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.services.UtenteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPageController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
class AdminPageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UtenteService utenteService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiCreaPage() throws Exception {
        mockMvc.perform(get("/admin/utenti/crea"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_crea"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiCreaSubmit() throws Exception {
        Utente newUser = new Utente();
        given(utenteService.create(any(Utente.class))).willReturn(newUser);
        given(passwordEncoder.encode("pass")).willReturn("encodedPass");

        mockMvc.perform(post("/admin/utenti/crea")
                        .param("username", "mario")
                        .param("email", "mario@mail.com")
                        .param("password", "pass")
                        .param("ruolo", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/utenti"));

        then(utenteService).should().create(any(Utente.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiListaPage() throws Exception {
        given(utenteService.findAll()).willReturn(java.util.List.of());

        mockMvc.perform(get("/admin/utenti"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_lista"))
                .andExpect(model().attributeExists("utenti"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiCercaPage() throws Exception {
        mockMvc.perform(get("/admin/utenti/cerca"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_cerca"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cercaById() throws Exception {
        Utente u = new Utente();
        given(utenteService.findById(1)).willReturn(u);

        mockMvc.perform(post("/admin/utenti/cerca/id").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_cerca"))
                .andExpect(model().attributeExists("byId"));

        then(utenteService).should().findById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cercaByUsername() throws Exception {
        Utente u = new Utente();
        given(utenteService.findByUsername("mario")).willReturn(u);

        mockMvc.perform(post("/admin/utenti/cerca/username")
                        .param("username", "mario"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_cerca"))
                .andExpect(model().attributeExists("byUsername"));

        then(utenteService).should().findByUsername("mario");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cercaByRuolo_shouldReturnResults() throws Exception {
        given(utenteService.findByRuolo(Ruolo.ADMIN))
                .willReturn(java.util.List.of());

        mockMvc.perform(post("/admin/utenti/cerca/ruolo")
                        .param("ruolo", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_cerca"))
                .andExpect(model().attributeExists("ruoloSelezionato"))
                .andExpect(model().attributeExists("utentiByRuolo"));

        then(utenteService).should().findByRuolo(Ruolo.ADMIN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiCancellaPage() throws Exception {
        given(utenteService.findById(5)).willReturn(new Utente());

        mockMvc.perform(get("/admin/utenti/cancella").param("id", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_cancella"))
                .andExpect(model().attributeExists("utente"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiCancellaSubmit() throws Exception {

        mockMvc.perform(post("/admin/utenti/cancella").param("id", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/utenti"));

        then(utenteService).should().deleteById(5);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiRipristinaPage() throws Exception {
        given(utenteService.findById(3)).willReturn(new Utente());

        mockMvc.perform(get("/admin/utenti/ripristina").param("id", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_ripristina"))
                .andExpect(model().attributeExists("utente"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiRipristinaSubmit() throws Exception {

        mockMvc.perform(post("/admin/utenti/ripristina").param("id", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/utenti"));

        then(utenteService).should().restoreDeletedColumn(3);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiModificaPage() throws Exception {
        given(utenteService.findById(2)).willReturn(new Utente());

        mockMvc.perform(get("/admin/utenti/modifica").param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/utenti_modifica"))
                .andExpect(model().attributeExists("utente"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void utentiModificaSubmit() throws Exception {

        mockMvc.perform(post("/admin/utenti/modifica")
                        .param("id", "1")
                        .param("username", "new")
                        .param("email", "mail@mail.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/utenti"));

        then(utenteService).should().update(any(Integer.class), any(Utente.class));
    }
}