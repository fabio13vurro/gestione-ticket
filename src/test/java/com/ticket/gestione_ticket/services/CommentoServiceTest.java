package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.*;
import com.ticket.gestione_ticket.repositories.CommentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CommentoServiceTest {
    @Mock private CommentoRepository commentoRepository;
    @Mock private TicketService ticketService;
    @Mock private UtenteService utenteService;

    @InjectMocks
    private CommentoService commentoService;

    @Test
    void createCommento(){
        Integer idTicket = 7;
        String testo = "testo";
        String tipo = "ESTERNO";
        String username = "Username";

        Ticket t = new Ticket();
        t.setIdTicket(idTicket);

        Utente u = new Utente();
        u.setUsername(username);
        u.setRuolo(Ruolo.OPERATORE);

        given(ticketService.findById(idTicket)).willReturn(t);
        given(utenteService.findByUsername(username)).willReturn(u);
        given(commentoRepository.save(any(Commento.class))).willAnswer(inv -> inv.getArgument(0));

        Commento c = commentoService.create(testo, tipo, idTicket, username);
        assertThat(c.getTesto()).isEqualTo(testo);
        assertThat(c.getTipo()).isEqualTo(Tipo.valueOf(tipo));
        assertThat(c.getTicket()).isSameAs(t);
        then(ticketService).should().findById(idTicket);
        then(utenteService).should().findByUsername(username);
        then(commentoRepository).should().save(any(Commento.class));
    }

    @Test
    void deleteCommentoById(){
        Commento c = new Commento();
        c.setDeleted(false);

        given(commentoRepository.findById(1)).willReturn(Optional.of(c));
        commentoService.deleteById(1);
        assertThat(c.getDeleted()).isTrue();
        then(commentoRepository).should().save(c);
    }

    @Test
    void deleteCommentoById_notFound(){
        given(commentoRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> commentoService.deleteById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Commento non trovato: 999");
    }

    @Test
    void ripristinaCommento(){
        Commento c = new Commento();
        c.setDeleted(true);

        given(commentoRepository.findById(1)).willReturn(Optional.of(c));
        commentoService.ripristina(1);
        assertThat(c.getDeleted()).isFalse();
        then(commentoRepository).should().save(c);
    }

    @Test
    void ripristinaCommento_notFound(){
        given(commentoRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> commentoService.ripristina(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Commento non trovato: 999");
    }

    @Test
    void updateCommento(){
        Commento old = new Commento();
        old.setTesto("vecchio");
        old.setDeleted(false);

        given(commentoRepository.findById(7)).willReturn(Optional.of(old));
        given(commentoRepository.save(any(Commento.class))).willAnswer(inv -> inv.getArgument(0));

        Commento updated = commentoService.update(7, "nuovo");
        assertThat(updated.getTesto()).isEqualTo("nuovo");
        assertThat(updated.getDeleted()).isFalse();
    }

    @Test
    void findById(){
        Commento c = new Commento();
        c.setIdCommento(10);

        given(commentoRepository.findById(10)).willReturn(Optional.of(c));
        Commento result = commentoService.findById(10);
        assertThat(result).isSameAs(c);
    }

    @Test
    void findByTipo(){
        Commento c = new Commento();
        c.setTipo(Tipo.INTERNO);

        given(commentoRepository.findByTipo(Tipo.INTERNO)).willReturn(List.of(c));
        List<Commento> result = commentoService.findByTipo(Tipo.INTERNO);
        assertThat(result).containsExactly(c);
    }
}