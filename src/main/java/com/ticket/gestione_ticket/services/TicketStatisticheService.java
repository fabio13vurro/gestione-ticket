package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.DTOs.TicketStatisticheDTO;
import com.ticket.gestione_ticket.mongodb.repositories.TicketStatisticheRepository;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketStatisticheService {

    private final TicketStatisticheRepository statisticheRepository;
    private final TicketRepository ticketRepository;

    public TicketStatisticheDTO getStatistiche(){
        List<TicketStatisticheDTO.StatisticheAnnoDTO> statisticheAnno = statisticheRepository.getStatistichePerAnno();

        int annoAttuale = Year.now().getValue();
        LocalDateTime inizioAnno = LocalDateTime.of(annoAttuale, 1, 1, 0, 0, 0);
        LocalDateTime fineAnno = LocalDateTime.of(annoAttuale, 12, 31, 23, 59, 59);
        long totale = ticketRepository.countTicketPerAnno(inizioAnno, fineAnno);
        long chiusi = ticketRepository.countTicketChiusiPerAnno(inizioAnno, fineAnno, "CHIUSO");
        double perc = totale > 0 ? Math.round((chiusi * 100.0 / totale) * 10.0) / 10.0 : 0.0;
        statisticheAnno.add(new TicketStatisticheDTO.StatisticheAnnoDTO(annoAttuale, totale, chiusi, perc, true));

        long totaleOperatori = ticketRepository.totaleOperatori();
        long operatoriConChiusi = ticketRepository.operatoriAttivi("CHIUSO");
        double percOperatori = totaleOperatori > 0
                ? Math.round((operatoriConChiusi * 100.0 / totaleOperatori) * 10.0) / 10.0
                : 0.0;

        return new TicketStatisticheDTO(
                statisticheAnno,
                new TicketStatisticheDTO.StatisticheOperatoriDTO(totaleOperatori, operatoriConChiusi, percOperatori)
        );
    }
}