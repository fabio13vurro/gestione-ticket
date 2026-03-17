package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.repositories.StoricoStatoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StoricoStatoServiceTest {
    @Mock
    private StoricoStatoRepository storicoStatoRepository;

    @InjectMocks
    private StoricoStatoService storicoStatoService;

    @Test
    void createStorico(){
        StoricoStato s = new StoricoStato();
        s.setStato_nuovo("stato");
        given(storicoStatoRepository.save(s)).willReturn(s);

        StoricoStato storico = storicoStatoService.create(s);
        assertThat(storico).isSameAs(s);
        then(storicoStatoRepository).should().save(s);
    }

    @Test
    void deleteStoricoById(){
        StoricoStato s = new StoricoStato();
        s.setDeleted(false);

        given(storicoStatoRepository.findById(1)).willReturn(Optional.of(s));
        storicoStatoService.deleteById(1);
        assertThat(s.getDeleted()).isTrue();
        then(storicoStatoRepository).should().save(s);
    }

    @Test
    void deleteStoricoById_notFound(){
        given(storicoStatoRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> storicoStatoService.deleteById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("StoricoStato non trovato: 999");
    }

    @Test
    void ripristinaStorico(){
        StoricoStato s = new StoricoStato();
        s.setDeleted(true);

        given(storicoStatoRepository.findById(1)).willReturn(Optional.of(s));
        storicoStatoService.ripristina(1);
        assertThat(s.getDeleted()).isFalse();
        then(storicoStatoRepository).should().save(s);
    }

    @Test
    void ripristinaStorico_notFound(){
        given(storicoStatoRepository.findById(999)).willReturn(Optional.empty());
        assertThatThrownBy(() -> storicoStatoService.ripristina(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("StoricoStato non trovato: 999");
    }

    @Test
    void updateStorico(){
        StoricoStato old = new StoricoStato();
        old.setStato_precedente("vecchio");
        old.setStato_nuovo("intermezzo");

        given(storicoStatoRepository.findById(7)).willReturn(Optional.of(old));
        given(storicoStatoRepository.save(any(StoricoStato.class))).willAnswer(inv -> inv.getArgument(0));

        StoricoStato newStorico = new StoricoStato();
        newStorico.setStato_nuovo("nuovo");

        StoricoStato updated = storicoStatoService.update(7, newStorico);
        assertThat(updated.getStato_precedente()).isEqualTo("vecchio");
        assertThat(updated.getStato_nuovo()).isEqualTo("nuovo");
    }

    @Test
    void findById(){
        StoricoStato s = new StoricoStato();
        s.setIdStorico_Stato(10);

        given(storicoStatoRepository.findById(10)).willReturn(Optional.of(s));
        StoricoStato result = storicoStatoService.findById(10);
        assertThat(result).isSameAs(s);
    }
}