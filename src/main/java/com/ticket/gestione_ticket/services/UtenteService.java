package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.DTOs.UtenteFileUploadRequest;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker(new Locale("it"));

    public Utente create(String username, String email, String password, String ruolo, String via, String citta) {
        Utente u = new Utente();

        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRuolo(Ruolo.valueOf(ruolo));
        u.setTicketAssegnati(0);
        u.setDeleted(false);
        u.setAddress(capitalize(via) + ", " + capitalize(citta));
        return utenteRepository.save(u);
    }

    public Utente aggiungiFileBase64(UtenteFileUploadRequest req){
        Utente u = utenteRepository.findByUsername(req.getUsername());
        if(u == null) throw new RuntimeException("Utente non trovato: " + req.getUsername());
        u.setFileData(req.getFileBase64());
        return utenteRepository.save(u);
    }

    public void deleteById(int id) {
        Utente utente = findById(id);

        if (utente.getRuolo() == Ruolo.OPERATORE) {
            List<Ticket> tickets = ticketRepository.findByUtenteAndStatoNot(utente, "CHIUSO");

            for(Ticket t : tickets){
                Utente operatore = utenteRepository.findByRuolo(Ruolo.OPERATORE)
                        .stream()
                        .filter(u -> !u.getDeleted() && !u.getIdUtente().equals(id))
                        .min(Comparator.comparing(Utente::getTicketAssegnati))
                        .orElseThrow(() -> new RuntimeException("Nessun operatore disponibile"));

                t.setUtente(operatore);
                operatore.setTicketAssegnati(operatore.getTicketAssegnati() + 1);
                utenteRepository.save(operatore);
                ticketRepository.save(t);
            }
        }

        utente.setTicketAssegnati(0);
        utente.setDeleted(true);
        utenteRepository.save(utente);
    }

    public void restoreDeletedColumn(Integer id){
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + id));

        utente.setDeleted(false);
        utenteRepository.save(utente);
    }

    @Transactional
    public Utente update(Integer id, String username, String email, String password, String via, String citta) {
        Utente utente = findById(id);
        boolean modifica = false;

        if(username != null && !username.isBlank() && !username.equals(utente.getUsername())) {
            utente.setUsername(username);
            modifica = true;
        }

        if(email != null && !email.isBlank() && !email.equals(utente.getEmail())) {
            utente.setEmail(email);
            modifica = true;
        }

        if(password != null && !password.isBlank() && !password.equals(utente.getPassword())) {
            modifica = true;
            utente.setPassword(passwordEncoder.encode(password));
        }

        boolean viaPresente = via != null && !via.isBlank();
        boolean cittaPresente = citta != null && !citta.isBlank();

        if(viaPresente && cittaPresente) {

            String newAddress = capitalize(via) + ", " + capitalize(citta);
            if(!newAddress.equals(utente.getAddress())) {
                modifica = true;
                utente.setAddress(newAddress);
            }
        } else if (viaPresente || cittaPresente) throw new RuntimeException("Entrambi i campi via e città devono essere presenti");

        if(!modifica) return utente;

        return utenteRepository.save(utente);
    }

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Utente findById(Integer id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + id));
    }

    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    public List<Utente> findByRuolo(Ruolo ruolo){
        return utenteRepository.findByRuolo(ruolo);
    }

    private String capitalize(String testo) {
        if (testo == null || testo.isBlank()) return testo;

        String[] parole = testo.toLowerCase().split("\\s+");
        StringBuilder risultato = new StringBuilder();

        for (String parola : parole) {
            if (!parola.isEmpty()) {
                risultato.append(Character.toUpperCase(parola.charAt(0)))
                        .append(parola.substring(1))
                        .append(" ");
            }
        }
        return risultato.toString().trim();
    }

    public void creazioneOperatore(){
        Utente u = new Utente();

        String base = faker.name().firstName();
        String username = base;

        int tentativi = 0;
        while (utenteRepository.existsByUsername(username)) {
            username = base + faker.number().numberBetween(1, 9999);
            if (++tentativi > 20) throw new RuntimeException("Impossibile generare username univoco");
        }
        u.setUsername(username);
        u.setEmail(u.getUsername() + "@gmail.com");
        u.setPassword(passwordEncoder.encode(u.getUsername()));
        u.setRuolo(Ruolo.OPERATORE);
        u.setTicketAssegnati(0);
        u.setDeleted(false);
        u.setAddress(capitalize(faker.address().streetAddress()) + ", " + capitalize(faker.address().city()));
        utenteRepository.save(u);
    }

    public void creazioneCliente(){
        Utente u = new Utente();

        String base = faker.name().firstName();
        String username = base;

        int tentativi = 0;
        while (utenteRepository.existsByUsername(username)) {
            username = base + faker.number().numberBetween(1, 9999);
            if (++tentativi > 20) throw new RuntimeException("Impossibile generare username univoco");
        }
        u.setUsername(username);
        u.setEmail(u.getUsername() + "@gmail.com");
        u.setPassword(passwordEncoder.encode(u.getUsername()));
        u.setRuolo(Ruolo.CLIENTE);
        u.setTicketAssegnati(0);
        u.setDeleted(false);
        u.setAddress(capitalize(faker.address().streetAddress()) + ", " + capitalize(faker.address().city()));
        utenteRepository.save(u);
    }

    public Page<Utente> getAll(Pageable pageable){
        return utenteRepository.findAll(pageable);
    }

    public Page<Utente> filtraUtenti(String username, String email, String ruolo, String ticketAssegnati, String address, Pageable pageable){
        return utenteRepository.filtraUtenti(username, email, ruolo, ticketAssegnati, address, pageable);
    }

    public boolean filtroAttivo(String username, String email, String ruolo, String ticketAssegnati, String address){
        return username != null || email != null || ruolo != null || ticketAssegnati != null || address != null;
    }

    public void rimuoviImmagine(String username) {
        Utente u = findByUsername(username);
        u.setFileData(null);
        utenteRepository.save(u);
    }
}