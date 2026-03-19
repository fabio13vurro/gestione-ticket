package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UtenteService utenteService;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicket() {
        var u = new Utente();
        u.setUsername("Username");
        given(utenteService.findByUsername("Username")).willReturn(u);

        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(inv -> inv.getArgument(0));

        Ticket result = ticketService.create("Test", "Descrizione", "Categoria", 3, "Username");

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        then(ticketRepository).should().save(captor.capture());
        Ticket saved = captor.getValue();

        assertThat(saved.getTitolo()).isEqualTo("Test");
        assertThat(saved.getDescrizione()).isEqualTo("Descrizione");
        assertThat(saved.getCategoria()).isEqualTo("Categoria");
        assertThat(saved.getPriorita()).isEqualTo(3);
        assertThat(saved.getStato()).isEqualTo("APERTO");
        assertThat(saved.getData_ora_apertura()).isNotNull();
        assertThat(saved.getOver_sla()).isFalse();
        assertThat(saved.getCreated()).isEqualTo("interno");
        assertThat(saved.getUtente()).isSameAs(u);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void deleteTicketById(){
        Ticket t = new Ticket();
        t.setDeleted(false);

        given(ticketRepository.findById(1)).willReturn(Optional.of(t));
        ticketService.deleteById(1);
        assertThat(t.getDeleted()).isTrue();
        then(ticketRepository).should().save(t);
    }

    @Test
    void deleteTicketById_notFound(){
        given(ticketRepository.findById(999)).willReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.deleteById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket non trovato: 999");
    }

    @Test
    void ripristinaTicket(){
        Ticket t = new Ticket();
        t.setDeleted(true);

        given(ticketRepository.findById(1)).willReturn(Optional.of(t));
        ticketService.ripristina(1);
        assertThat(t.getDeleted()).isFalse();
        then(ticketRepository).should().save(t);
    }

    @Test
    void ripristinaTicket_notFound(){
        given(ticketRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.ripristina(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket non trovato: 999");
    }

    @Test
    void updateTicket(){
        Ticket old = new Ticket();
        old.setTitolo("Vecchio");
        old.setDescrizione("Vecchia descrizione");
        old.setCategoria("Vecchia categoria");
        old.setStato("APERTO");
        old.setPriorita(2);

        given(ticketRepository.findById(7)).willReturn(Optional.of(old));
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> inv.getArgument(0));

        Ticket updated = ticketService.update(7, "Nuovo", null, null, 3);
        assertThat(updated.getTitolo()).isEqualTo("Nuovo");
        assertThat(updated.getPriorita()).isEqualTo(3);

        assertThat(updated.getDescrizione()).isEqualTo("Vecchia descrizione");
        assertThat(updated.getCategoria()).isEqualTo("Vecchia categoria");
        assertThat(updated.getStato()).isEqualTo("APERTO");

        then(ticketRepository).should().save(updated);
    }

    @Test
    void findById(){
        Ticket t = new Ticket();
        t.setIdTicket(10);

        given(ticketRepository.findById(10)).willReturn(Optional.of(t));

        Ticket result = ticketService.findById(10);
        assertThat(result).isSameAs(t);
    }

    @Test
    void findById_notFound(){
        given(ticketRepository.findById(999)).willReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.findById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket non trovato: 999");
    }

    @Test
    void findByTitolo(){
        Ticket t = new Ticket();
        t.setTitolo("titolo");

        given(ticketRepository.findByTitolo("titolo")).willReturn(t);
        Ticket result = ticketService.findByTitolo("titolo");
        assertThat(result).isSameAs(t);
    }

    @Test
    void creazioneTicketScheduled() {
        given(ticketRepository.save(any(Ticket.class)))
                .willAnswer(inv -> inv.getArgument(0));

        Ticket t = ticketService.creazioneTicket("titolo", "descrizione");

        assertThat(t.getTitolo()).isEqualTo("titolo");
        assertThat(t.getDescrizione()).isEqualTo("descrizione");
        assertThat(t.getCategoria()).isEqualTo("MONITORAGGIO");
        assertThat(t.getPriorita()).isEqualTo(3);
        assertThat(t.getStato()).isEqualTo("APERTO");
        assertThat(t.getData_ora_apertura()).isNotNull();
        assertThat(t.getData_ora_chiusura()).isNull();
        assertThat(t.getOver_sla()).isFalse();
        assertThat(t.getDeleted()).isFalse();
        assertThat(t.getCreated()).isEqualTo("esterno");

        then(ticketRepository).should().save(any(Ticket.class));
    }
}