package com.ticket.gestione_ticket.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.gestione_ticket.config.HttpClientProperties;
import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import com.ticket.gestione_ticket.services.HttpClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserImportJob{

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRateString = "${scheduler.fixedrate}")
    public void importUsers() {
        try {
            String query = Files.readString(Paths.get("src/main/resources/graphql/queries/user-import.graphql"));

            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.createObjectNode().put("query", query).toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);

            String url = "http://localhost:8085/graphql";
            String response = restTemplate.postForObject(url, entity, String.class);

            JsonNode root = mapper.readTree(response);
            JsonNode users = root.path("data").path("users");

            for (JsonNode u : users) {

                String username = u.path("username").asText();
                String email = u.path("email").asText();
                String street = u.path("address").path("street").asText();
                String city = u.path("address").path("city").asText();

                if(utenteRepository.existsByEmail(email) || utenteRepository.existsByUsername(username)) continue;

                Utente utente = new Utente();
                utente.setUsername(username);
                utente.setEmail(email);
                utente.setPassword(passwordEncoder.encode(username));
                Ruolo ruolo = random.nextBoolean() ? Ruolo.CLIENTE : Ruolo.OPERATORE;
                utente.setRuolo(ruolo);
                utente.setLibero(true);
                utente.setDeleted(false);
                utente.setAddress(street + ", " + city);

                utenteRepository.save(utente);
            }

            System.out.println("Importazione utenti completata");
        }catch (Exception e){
            System.err.println("Errore nel recupero dei dati utente: " + e.getMessage());
        }
    }
}