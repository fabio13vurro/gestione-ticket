package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.jobs.JobProducer;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private JobProducer jobProducer;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicket(){
        Ticket t = new Ticket();
        t.setTitolo("Test");
        given(ticketRepository.save(t)).willReturn(t);

        Ticket ticket = ticketService.create(t);
        assertThat(ticket).isSameAs(t);
        then(ticketRepository).should().save(t);
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

        Ticket newTicket = new Ticket();
        newTicket.setTitolo("Nuovo");
        newTicket.setStato("CHIUSO");

        Ticket updated = ticketService.update(7, newTicket);
        assertThat(updated.getTitolo()).isEqualTo("Nuovo");
        assertThat(updated.getStato()).isEqualTo("CHIUSO");

        assertThat(updated.getDescrizione()).isEqualTo("Vecchia descrizione");
        assertThat(updated.getCategoria()).isEqualTo("Vecchia categoria");
        assertThat(updated.getPriorita()).isEqualTo(2);
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
    void creazioneTicketScheduled(){
        Ticket t = ticketService.creazioneTicket("titolo", "descrizione");
        assertThat(t.getTitolo()).isEqualTo("titolo");
        assertThat(t.getDescrizione()).isEqualTo("descrizione");
        assertThat(t.getCategoria()).isEqualTo("MONITORAGGIO");
        assertThat(t.getPriorita()).isEqualTo(3);
        assertThat(t.getStato()).isEqualTo("APERTO");
        assertThat(t.getData_ora_apertura()).isNotNull();
        assertThat(t.getData_ora_chiusura()).isNotNull();
        assertThat(t.getOver_sla()).isFalse();
        assertThat(t.getDeleted()).isFalse();
        assertThat(t.getSla()).isEqualTo(1);
        assertThat(t.getCreated()).isEqualTo("esterno");
    }
}