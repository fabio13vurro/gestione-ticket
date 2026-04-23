package com.ticket.gestione_ticket.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatisticheDTO {

    private List<StatisticheAnnoDTO> statistichePerAnno;

    private StatisticheOperatoriDTO statisticheOperatori;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatisticheAnnoDTO {
        private Integer anno;
        private Long totaleTicket;
        private Long ticketChiusi;
        private Double percentualeChiusi;
        private boolean daMySQL;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatisticheOperatoriDTO {
        private Long totaleOperatoriDistinti;
        private Long operatoriConTicketChiuso;
        private Double percentualeOperatori;
    }
}