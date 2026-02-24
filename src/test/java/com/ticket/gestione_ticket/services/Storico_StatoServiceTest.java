package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Storico_Stato;
import com.ticket.gestione_ticket.repositories.Storico_StatoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class Storico_StatoServiceTest {
    @Mock
    private Storico_StatoRepository storico_statoRepository;

    @InjectMocks
    private Storico_StatoService storico_StatoService;

    @Test
    void createStorico(){
        Storico_Stato s = new Storico_Stato();
        s.setStato_nuovo("stato");
        given(storico_statoRepository.save(s)).willReturn(s);

        Storico_Stato storico = storico_StatoService.create(s);
        assertThat(storico).isSameAs(s);
        then(storico_statoRepository).should().save(s);
    }

    @Test
    void deleteStoricoById(){
        Storico_Stato s = new Storico_Stato();
        s.setDeleted(false);

        given(storico_statoRepository.findById(1)).willReturn(Optional.of(s));
        storico_StatoService.deleteById(1);
        assertThat(s.getDeleted()).isTrue();
        then(storico_statoRepository).should().save(s);
    }

    @Test
    void deleteStoricoById_notFound(){
        given(storico_statoRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> storico_StatoService.deleteById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Storico_Stato non trovato: 999");
    }

    @Test
    void ripristinaStorico(){
        Storico_Stato s = new Storico_Stato();
        s.setDeleted(true);

        given(storico_statoRepository.findById(1)).willReturn(Optional.of(s));
        storico_StatoService.ripristina(1);
        assertThat(s.getDeleted()).isFalse();
        then(storico_statoRepository).should().save(s);
    }

    @Test
    void ripristinaStorico_notFound(){
        given(storico_statoRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> storico_StatoService.ripristina(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Storico_Stato non trovato: 999");
    }

    @Test
    void updateStorico(){
        Storico_Stato old = new Storico_Stato();
        old.setStato_precedente("vecchio");
        old.setStato_nuovo("intermezzo");

        given(storico_statoRepository.findById(7)).willReturn(Optional.of(old));
        given(storico_statoRepository.save(any(Storico_Stato.class))).willAnswer(inv -> inv.getArgument(0));

        Storico_Stato newStorico = new Storico_Stato();
        newStorico.setStato_nuovo("nuovo");

        Storico_Stato updated = storico_StatoService.update(7, newStorico);
        assertThat(updated.getStato_precedente()).isEqualTo("vecchio");
        assertThat(updated.getStato_nuovo()).isEqualTo("nuovo");
    }

    @Test
    void findById(){
        Storico_Stato s = new Storico_Stato();
        s.setIdStorico_Stato(10);

        given(storico_statoRepository.findById(10)).willReturn(Optional.of(s));
        Storico_Stato result = storico_StatoService.findById(10);
        assertThat(result).isSameAs(s);
    }
}