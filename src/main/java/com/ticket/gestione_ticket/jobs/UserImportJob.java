package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserImportJob{
    private final UtenteRepository utenteRepository;
    private final Random random = new Random();
    ObjectMapper mapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();
    private final PasswordEncoder passwordEncoder;

    @Scheduled(fixedRateString = "${scheduler.fixedrate}")
    public void importUsers() {
        String url = "https://jsonplaceholder.typicode.com/users";

        try {
            String json = restTemplate.getForObject(url, String.class);
            List<Map<String, Object>> lista = mapper.readValue(json, new TypeReference<>() {});

            for (Map<String, Object> utenteJson : lista) {
                Utente utente = new Utente();
                utente.setUsername((String) utenteJson.get("username"));
                utente.setEmail((String) utenteJson.get("email"));
                if (utenteRepository.existsByEmail(utente.getEmail()) || utenteRepository.existsByUsername(utente.getUsername())) continue;
                utente.setPassword(passwordEncoder.encode(utente.getUsername()));
                Ruolo ruolo = random.nextBoolean() ? Ruolo.CLIENTE : Ruolo.OPERATORE ;
                utente.setRuolo(ruolo);
                utente.setLibero(true);
                utente.setDeleted(false);
                utenteRepository.save(utente);
            }

            System.out.println("Importazione utenti completata");
        }catch (Exception e){
            System.err.println("Errore nel recupero dei dati utente: " + e.getMessage());
        }
    }
}