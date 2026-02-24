package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.entities.Tipo;
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
    @Mock
    private CommentoRepository commentoRepository;

    @InjectMocks
    private CommentoService commentoService;

    @Test
    void createCommento(){
        Commento c = new Commento();
        c.setTesto("testo");
        given(commentoRepository.save(c)).willReturn(c);

        Commento commento = commentoService.create(c);
        assertThat(commento).isSameAs(c);
        then(commentoRepository).should().save(c);
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
        old.setTipo(Tipo.ESTERNO);
        old.setDeleted(false);

        given(commentoRepository.findById(7)).willReturn(Optional.of(old));
        given(commentoRepository.save(any(Commento.class))).willAnswer(inv -> inv.getArgument(0));

        Commento newCommento = new Commento();
        newCommento.setTesto("nuovo");
        newCommento.setTipo(Tipo.INTERNO);

        Commento updated = commentoService.update(7, newCommento);
        assertThat(updated.getTesto()).isEqualTo("nuovo");
        assertThat(updated.getTipo()).isEqualTo(Tipo.INTERNO);
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