package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.mongodb.documents.TicketDocument;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiviazioneTicketService {

    static final String COL_CHIUSI     = "tickets_chiusi";
    static final String COL_CROSS_ANNO = "tickets_cross_anno";

    private final TicketRepository ticketRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public void archiviaTutti() {
        int soglia = LocalDate.now().getYear() - 1;
        List<Ticket> tickets = ticketRepository.findTicketDaArchiviareConCommenti(soglia);
        ticketRepository.findTicketDaArchiviareConStorici(soglia);

        int chiusi = 0, crossAnno = 0, saltati = 0;

        for (Ticket t : tickets) {
            if (t.getData_ora_chiusura() == null) {
                saltati++;
                continue;
            }

            int annoApertura = t.getData_ora_apertura().getYear();
            int annoChiusura = t.getData_ora_chiusura().getYear();

            if (annoChiusura > soglia) {
                saltati++;
                continue;
            }

            if (annoApertura != annoChiusura) {
                salvaInCollezione(t, annoApertura, annoChiusura, COL_CROSS_ANNO);
                crossAnno++;
            } else {
                salvaInCollezione(t, annoApertura, annoChiusura, COL_CHIUSI);
                chiusi++;
            }
        }

        log.info("Fine archiviazione — tickets_chiusi: {}, tickets_cross_anno: {}, saltati: {}",
                chiusi, crossAnno, saltati);
    }

    private void salvaInCollezione(Ticket t, int annoApertura, int annoChiusura, String collezione) {
        if(mongoTemplate.exists(
                Query.query(Criteria.where("idTicket").is(t.getIdTicket())),
                collezione)
        ) return;

        mongoTemplate.save(toDocument(t, annoApertura, annoChiusura), collezione);
    }

    private TicketDocument toDocument(Ticket t, int annoApertura, int annoChiusura) {
        TicketDocument doc = new TicketDocument();
        doc.setIdTicket(t.getIdTicket());
        doc.setTitolo(t.getTitolo());
        doc.setDescrizione(t.getDescrizione());
        doc.setCategoria(t.getCategoria());
        doc.setPriorita(t.getPriorita());
        doc.setStato(t.getStato());
        doc.setDataOraApertura(t.getData_ora_apertura());
        doc.setDataOraChiusura(t.getData_ora_chiusura());
        doc.setDataOraScadenza(t.getData_ora_scadenza());
        doc.setOverSla(t.getOver_sla());
        doc.setAnnoApertura(annoApertura);
        doc.setAnnoChiusura(annoChiusura);
        doc.setArchiviatoIl(LocalDateTime.now());

        if (t.getUtente() != null) {
            doc.setUtente(new TicketDocument.UtenteSnapshot(
                    t.getUtente().getIdUtente(),
                    t.getUtente().getUsername(),
                    t.getUtente().getEmail(),
                    t.getUtente().getRuolo().name()
            ));
        }

        doc.setCommenti(t.getCommenti().stream()
                .filter(c -> !Boolean.TRUE.equals(c.getDeleted()))
                .map(c -> new TicketDocument.CommentoSnapshot(
                        c.getIdCommento(),
                        c.getTesto(),
                        c.getTipo().name(),
                        c.getData_ora(),
                        c.getCreated()
                ))
                .collect(Collectors.toList()));

        doc.setStoricoStati(t.getStorici().stream()
                .filter(s -> !Boolean.TRUE.equals(s.getDeleted()))
                .map(s -> new TicketDocument.StoricoStatoSnapshot(
                        s.getIdStoricoStato(),
                        s.getStato_precedente(),
                        s.getStato_nuovo(),
                        s.getData_ora()
                ))
                .collect(Collectors.toList()));

        return doc;
    }
}