package com.ticket.gestione_ticket.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.gestione_ticket.config.JobQueueConfig;
import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.jobs.JobProducer;
import com.ticket.gestione_ticket.mappers.TicketMapper;
import com.ticket.gestione_ticket.mongodb.documents.OverSlaTicketDocument;
import com.ticket.gestione_ticket.mongodb.repositories.OverSlaTicketRepository;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OverSlaServiceTest {

    @Mock TicketRepository ticketRepository;
    @Mock OverSlaTicketRepository overSlaTicketRepository;
    @Mock TicketMapper ticketMapper;
    @Mock ObjectMapper objectMapper;
    @Mock JobProducer jobProducer;

    @InjectMocks
    OverSlaService service;

    private Ticket makeTicket(Integer id, boolean over, LocalDateTime ultimoAgg) {
        Ticket t = new Ticket();
        t.setIdTicket(id);
        t.setOver_sla(over);
        t.setData_ora_apertura(LocalDateTime.now().minusHours(50));
        t.setDeleted(false);

        if (ultimoAgg != null) {
            StoricoStato ss = new StoricoStato();
            ss.setData_ora(ultimoAgg);
            t.setStorici(List.of(ss));
        }

        return t;
    }

    @Test
    void ticketGiaOverSla() {
        Ticket t = makeTicket(1, true, LocalDateTime.now().minusHours(10));

        OverSlaTicketDocument doc = new OverSlaTicketDocument();
        when(ticketRepository.findAllWithCommentiAndStorico()).thenReturn(List.of(t));
        when(ticketMapper.toMongoSnapshot(any(), any(), any())).thenReturn(doc);

        service.controlloOverSla();

        verify(overSlaTicketRepository, times(1)).save(doc);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void ticketDiventaOverSla() {
        LocalDateTime vecchio = LocalDateTime.now().minusHours(60);
        Ticket t = makeTicket(2, false, vecchio);

        OverSlaTicketDocument doc = new OverSlaTicketDocument();

        when(ticketRepository.findAllWithCommentiAndStorico()).thenReturn(List.of(t));
        when(ticketMapper.toMongoSnapshot(any(), any(), any())).thenReturn(doc);

        service.controlloOverSla();

        assertThat(t.getOver_sla()).isTrue();

        verify(ticketRepository).save(t);
        verify(overSlaTicketRepository).save(doc);
    }

    @Test
    void ticketNonOverSlaNonVecchio() {
        Ticket t = new Ticket();
        t.setIdTicket(3);
        t.setOver_sla(false);
        t.setData_ora_apertura(LocalDateTime.now().minusHours(5));

        when(ticketRepository.findAllWithCommentiAndStorico()).thenReturn(List.of(t));

        service.controlloOverSla();

        verify(ticketMapper, never()).toMongoSnapshot(any(), any(), any());
        verify(overSlaTicketRepository, never()).save(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void mongoGiu() throws Exception {
        Ticket t = makeTicket(5, true, LocalDateTime.now().minusHours(10));
        OverSlaTicketDocument doc = new OverSlaTicketDocument();

        when(ticketRepository.findAllWithCommentiAndStorico()).thenReturn(List.of(t));
        when(ticketMapper.toMongoSnapshot(any(), any(), any())).thenReturn(doc);

        doThrow(new RuntimeException("Mongo down")).when(overSlaTicketRepository).save(doc);
        when(objectMapper.writeValueAsString(doc)).thenReturn("{json}");
        service.controlloOverSla();
        verify(jobProducer).sendJob(JobQueueConfig.MONGO_QUEUE, "{json}");
    }

    @Test
    void jsonError() throws Exception {
        Ticket t = makeTicket(6, true, LocalDateTime.now().minusHours(10));
        OverSlaTicketDocument doc = new OverSlaTicketDocument();

        when(ticketRepository.findAllWithCommentiAndStorico()).thenReturn(List.of(t));
        when(ticketMapper.toMongoSnapshot(any(), any(), any())).thenReturn(doc);

        doThrow(new RuntimeException("Mongo down")).when(overSlaTicketRepository).save(doc);
        doThrow(new RuntimeException("JSON error")).when(objectMapper).writeValueAsString(doc);

        assertThatCode(() -> service.controlloOverSla()).doesNotThrowAnyException();
        verify(jobProducer, never()).sendJob(any(), any());
    }
}