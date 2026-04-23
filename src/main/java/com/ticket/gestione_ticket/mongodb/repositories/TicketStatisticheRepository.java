package com.ticket.gestione_ticket.mongodb.repositories;

import com.ticket.gestione_ticket.DTOs.TicketStatisticheDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class TicketStatisticheRepository {

    private final MongoTemplate mongoTemplate;

    private static final String COLLECTION = "tickets_chiusi";

    public List<TicketStatisticheDTO.StatisticheAnnoDTO> getStatistichePerAnno() {

        Aggregation aggTotale = Aggregation.newAggregation(
                Aggregation.group("annoApertura").count().as("totale"),
                Aggregation.project("totale").and("_id").as("anno"),
                Aggregation.sort(Sort.Direction.ASC, "anno")
        );
        List<Map> totalePerAnno = mongoTemplate.aggregate(aggTotale, COLLECTION, Map.class).getMappedResults();

        Aggregation aggChiusi = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("stato").is("CHIUSO")),
                Aggregation.group("annoChiusura").count().as("chiusi"),
                Aggregation.project("chiusi").and("_id").as("anno")
        );
        List<Map> chiusiPerAnno = mongoTemplate.aggregate(aggChiusi, COLLECTION, Map.class).getMappedResults();

        Map<Integer, Long> mapChiusi = new HashMap<>();
        for (Map m : chiusiPerAnno) {
            if (m.get("anno") != null)
                mapChiusi.put((Integer) m.get("anno"), ((Number) m.get("chiusi")).longValue());
        }

        List<TicketStatisticheDTO.StatisticheAnnoDTO> result = new ArrayList<>();
        for (Map m : totalePerAnno) {
            if (m.get("anno") == null) continue;
            Integer anno = (Integer) m.get("anno");
            long totale = ((Number) m.get("totale")).longValue();
            long chiusi = mapChiusi.getOrDefault(anno, 0L);
            double perc = totale > 0 ? Math.round((chiusi * 100.0 / totale) * 10.0) / 10.0 : 0.0;
            result.add(new TicketStatisticheDTO.StatisticheAnnoDTO(anno, totale, chiusi, perc, false));
        }

        result.sort(Comparator.comparing(TicketStatisticheDTO.StatisticheAnnoDTO::getAnno));
        return result;
    }
}