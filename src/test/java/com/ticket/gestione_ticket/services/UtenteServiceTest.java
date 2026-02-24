package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceTest {
    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private UtenteService utenteService;

    @Test
    void createUtente(){
        Utente u = new Utente();
        u.setUsername("test");
        given(utenteRepository.save(u)).willReturn(u);

        Utente utente = utenteService.create(u);
        assertThat(utente).isSameAs(u);
        then(utenteRepository).should().save(u);
    }

    @Test
    void deleteUtenteById(){
        Utente u = new Utente();
        u.setDeleted(false);

        given(utenteRepository.findById(1)).willReturn(Optional.of(u));
        utenteService.deleteById(1);
        assertThat(u.getDeleted()).isTrue();
        then(utenteRepository).should().save(u);
    }

    @Test
    void deleteUtenteById_notFound(){
        given(utenteRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> utenteService.deleteById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utente non trovato: 999");
    }

    @Test
    void ripristinaUtente(){
        Utente u = new Utente();
        u.setDeleted(true);

        given(utenteRepository.findById(1)).willReturn(Optional.of(u));
        utenteService.restoreDeletedColumn(1);
        assertThat(u.getDeleted()).isFalse();
        then(utenteRepository).should().save(u);
    }

    @Test
    void ripristinaUtente_notFound(){
        given(utenteRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> utenteService.restoreDeletedColumn(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utente non trovato: 999");
    }

    @Test
    void updateUtente(){
        Utente old = new Utente();
        old.setEmail("email");
        old.setUsername("username");
        old.setPassword("PASSWORD");
        old.setLibero(true);

        given(utenteRepository.findById(7)).willReturn(Optional.of(old));
        given(utenteRepository.save(any(Utente.class))).willAnswer(inv -> inv.getArgument(0));

        Utente newUtente = new Utente();
        newUtente.setEmail("email2");
        newUtente.setUsername("username2");

        Utente updated = utenteService.update(7, newUtente);
        assertThat(updated.getEmail()).isEqualTo("email2");
        assertThat(updated.getUsername()).isEqualTo("username2");
        assertThat(updated.getPassword()).isEqualTo("PASSWORD");
        assertThat(updated.getLibero()).isTrue();
    }

    @Test
    void findById(){
        Utente u = new Utente();
        u.setIdUtente(10);

        given(utenteRepository.findById(10)).willReturn(Optional.of(u));
        Utente result = utenteService.findById(10);
        assertThat(result).isSameAs(u);
    }

    @Test
    void findById_notFound(){
        given(utenteRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> utenteService.findById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utente non trovato: 999");
    }

    @Test
    void findByUsername(){
        Utente u = new Utente();
        u.setUsername("username");
        given(utenteRepository.findByUsername("username")).willReturn(u);
        Utente result = utenteService.findByUsername("username");
        assertThat(result).isSameAs(u);
    }

    @Test
    void findByRuolo(){
        Utente u = new Utente();
        u.setRuolo(Ruolo.ADMIN);
        given(utenteRepository.findByRuolo(Ruolo.ADMIN)).willReturn(List.of(u));
        List<Utente> result = utenteService.findByRuolo(Ruolo.ADMIN);
        assertThat(result).containsExactly(u);
    }
}