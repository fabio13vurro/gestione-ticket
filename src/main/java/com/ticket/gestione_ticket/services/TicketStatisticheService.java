package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.DTOs.TicketStatisticheDTO;
import com.ticket.gestione_ticket.mongodb.repositories.TicketStatisticheRepository;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public TicketStatisticheDTO.StatisticheOperatoreRicercaDTO getStatisticheOperatore(String username){
        List<Object[]> risultati = ticketRepository.ticketOperatorePerStato(username);
        if (risultati.isEmpty()) return null;

        Map<String, Long> ticketPerStato = new LinkedHashMap<>();
        ticketPerStato.put("APERTO", 0L);
        ticketPerStato.put("IN_LAVORAZIONE", 0L);
        ticketPerStato.put("IN_ATTESA", 0L);
        ticketPerStato.put("RISOLTO", 0L);
        ticketPerStato.put("CHIUSO", 0L);
        ticketPerStato.put("SCADUTO", 0L);

        long totale = 0L;
        for(Object[] row : risultati){
            String stato = row[0].toString();
            long count = (long) row[1];
            ticketPerStato.put(stato, count);
            totale += count;
        }

        return new TicketStatisticheDTO.StatisticheOperatoreRicercaDTO(username, totale, ticketPerStato);
    }
}