package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.config.HttpClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class HttpClientService {

    private final RestTemplate restTemplate;
    private final HttpClientProperties properties;

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 10000),
            listeners = {"retryLogger"}
    )
    public String getJson(String url) {
        System.out.println("Tentativo di recupero dati da: " + url);
        return restTemplate.getForObject(url, String.class);
    }
}