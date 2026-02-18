package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import com.ticket.gestione_ticket.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserImportJob{
    private final UtenteRepository utenteRepository;
    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();
    private final PasswordEncoder passwordEncoder;

    @Scheduled(fixedRateString = "${scheduler.fixedrate}")
    public void importUsers() {
        String url = "https://jsonplaceholder.typicode.com/users";

        try {
            String json = getJsonWithRetry(url);
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

    private String getJsonWithRetry(String url) {

        int tent = 0, maxTent = 3, timeout = 10000;
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);

        RestTemplate rt = new RestTemplate(factory);

        while (tent < maxTent) {
            try {
                return rt.getForObject(url, String.class);
            } catch (Exception ex) {
                tent++;
                System.err.println("Tentativo " + tent + " fallito (" + ex.getMessage() + ")");

                if (tent == maxTent) {
                    throw new RuntimeException("URL non raggiungibile dopo 3 tentativi: " + url);
                }

                try {
                    Thread.sleep(timeout);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}