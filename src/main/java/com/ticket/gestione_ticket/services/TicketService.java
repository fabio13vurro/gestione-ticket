package com.ticket.gestione_ticket.services;

import com.mongodb.lang.Nullable;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StoricoStatoService storicoService;
    private final UtenteRepository utenteRepository;
    private final Faker faker = new Faker(new Locale("it"));

    public Ticket create(String titolo, String descr, String categoria, Integer priorita, String username) {
        Ticket t = new Ticket();
        t.setTitolo(titolo);
        t.setDescrizione(descr);
        t.setCategoria(categoria);
        t.setPriorita(priorita);
        t.setStato("APERTO");
        t.setData_ora_apertura(LocalDateTime.now());
        t.setOver_sla(false);
        t.setCreated(username);
        t.setDeleted(false);
        t.setData_ora_scadenza(calcolaScadenza(priorita));
        assegnaTicket(t);
        return ticketRepository.save(t);
    }

    public LocalDateTime calcolaScadenza(Integer priorita) {
        LocalDateTime apertura = LocalDateTime.now();
        return switch (priorita) {
            case 1 -> apertura.plusHours(12);
            case 2 -> apertura.plusDays(1);
            case 3 -> apertura.plusHours(36);
            default -> apertura.plusDays(2);
        };
    }

    public void controlloScadenze() {
        LocalDateTime now = LocalDateTime.now();
        List<Ticket> tickets = ticketRepository.findAll();
        for (Ticket t : tickets) {
            if (t.getOver_sla().equals(true) || t.getStato().equals("CHIUSO") || t.getDeleted().equals(true) || t.getStato().equals("IN_ATTESA")) {
                continue;
            } else {
                if (t.getData_ora_scadenza() != null && now.isAfter(t.getData_ora_scadenza())) {
                    t.setOver_sla(true);
                    t.setStato("SCADUTO");
                    ticketRepository.save(t);
                }
            }
        }
    }

    public void deleteById(int id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));

        ticket.setDeleted(true);
        ticketRepository.save(ticket);
    }

    public void ripristina(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));

        ticket.setDeleted(false);
        ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket update(Integer id, @Nullable String titolo, @Nullable String descrizione, @Nullable String categoria,
                         @Nullable Integer priorita) {
        Ticket ticket = findById(id);

        boolean modifica = false;

        if (titolo != null && !titolo.isBlank() && !titolo.equals(ticket.getTitolo())) {
            ticket.setTitolo(titolo);
            modifica = true;
        }

        if (descrizione != null && !descrizione.isBlank() && !descrizione.equals(ticket.getDescrizione())) {
            ticket.setDescrizione(descrizione);
            modifica = true;
        }

        if (categoria != null && !categoria.isBlank() && !categoria.equals(ticket.getCategoria())) {
            ticket.setCategoria(categoria);
            modifica = true;
        }

        if (priorita != null && !priorita.equals(ticket.getPriorita())
                && priorita > 0 && priorita < 5) {
            ticket.setPriorita(priorita);
            modifica = true;
        }

        if (!modifica) return ticket;

        return ticketRepository.save(ticket);
    }

    public Ticket cambiaStato(Integer id) {
        Ticket t = findById(id);
        if (t == null) return null;

        String statoNuovo, statoAttuale = t.getStato();

        if (statoAttuale.equals("CHIUSO")) return t;

        switch (statoAttuale) {
            case "APERTO":
                statoNuovo = "IN_LAVORAZIONE";
                break;
            case "IN_LAVORAZIONE":
                statoNuovo = "IN_ATTESA";
                break;
            case "IN_ATTESA":
                statoNuovo = "RISOLTO";
                break;
            case "RISOLTO":
                statoNuovo = "CHIUSO";
                t.setData_ora_chiusura(LocalDateTime.now());
                Utente operatore = t.getUtente();
                if (operatore != null) {
                    operatore.setTicketAssegnati(operatore.getTicketAssegnati() - 1);
                    utenteRepository.save(operatore);
                }
                break;
            case "SCADUTO":
                statoNuovo = "APERTO";
                t.setData_ora_scadenza(calcolaScadenza(t.getPriorita()));
                break;
            default:
                statoNuovo = statoAttuale;
        }

        storicoService.create(t, statoAttuale, statoNuovo);
        t.setStato(statoNuovo);
        return ticketRepository.save(t);
    }

    public void assegnaTicket(Ticket t) {
        List<Utente> operatori = utenteRepository.findByRuolo(Ruolo.OPERATORE);
        Utente o = operatori.stream()
                .filter(u -> !u.getDeleted())
                .min(Comparator.comparing(Utente::getTicketAssegnati))
                .orElseThrow(() -> new RuntimeException("Nessun operatore disponibile"));

        t.setUtente(o);
        o.setTicketAssegnati(o.getTicketAssegnati() + 1);
        utenteRepository.save(o);
    }

    public Ticket creazioneTicket() {
        String[] categorie = {"HARDWARE", "SOFTWARE", "RETE", "SICUREZZA"};

        String titolo = faker.lorem().sentence(4);
        String descr = faker.lorem().paragraph(2);
        String categoria = categorie[faker.number().numberBetween(0, categorie.length)];
        Integer priorita = faker.number().numberBetween(1, 5);

        List<Utente> clienti = utenteRepository.findByRuolo(Ruolo.CLIENTE);
        if (clienti.isEmpty()) throw new RuntimeException("Nessun cliente disponibile");
        Utente cliente = clienti.get(faker.number().numberBetween(0, clienti.size()));

        return create(titolo, descr, categoria, priorita, cliente.getUsername());
    }

    public Page<Ticket> getAll(Pageable pageable) {
        return ticketRepository.findAllWithCommenti(pageable);
    }

    public Page<Ticket> filtraTicket(String titolo, String descrizione, String categoria, String stato, String priorita, String username, LocalDateTime dataAperturaDa, LocalDateTime dataAperturaA, LocalDateTime dataChiusuraDa, LocalDateTime dataChiusuraA, Integer numCommenti, String created, Pageable pageable) {
        return ticketRepository.filtraTicket(titolo, descrizione, categoria, stato, priorita, username, dataAperturaDa, dataAperturaA, dataChiusuraDa, dataChiusuraA, numCommenti, created, pageable);
    }

    public boolean filtroAttivo(String titolo, String descrizione, String categoria, String stato, String priorita, String username, LocalDateTime dataAperturaDa, LocalDateTime dataAperturaA, LocalDateTime dataChiusuraDa, LocalDateTime dataChiusuraA, Integer numCommenti, String created) {
        return titolo != null || descrizione != null || categoria != null
                || stato != null || priorita != null || username != null || dataAperturaDa != null
                || dataAperturaA != null || dataChiusuraDa != null || dataChiusuraA != null
                || numCommenti != null || created != null;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Page<Ticket> ticketScaduti(Pageable pageable) {
        return ticketRepository.findByOverSlaTrue(pageable);
    }

    public Ticket findById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato: " + id));
    }

    public Ticket findByTitolo(String titolo) {
        return ticketRepository.findByTitolo(titolo);
    }

    public List<Ticket> findByStato(String stato) {
        return ticketRepository.findByStato(stato);
    }

    public List<Ticket> findByPriorita(int priorita) {
        return ticketRepository.findByPriorita(priorita);
    }

    public List<Ticket> findByCategoria(String categoria) {
        return ticketRepository.findByCategoria(categoria);
    }

    public List<Ticket> findByCreated(String username) {
        return ticketRepository.findByCreated(username);
    }

    public Page<Ticket> findByCreated(String username, Pageable pageable) {
        return ticketRepository.findByCreated(username, pageable);
    }
}