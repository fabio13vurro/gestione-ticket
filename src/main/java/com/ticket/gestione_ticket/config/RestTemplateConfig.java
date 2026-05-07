package com.ticket.gestione_ticket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.metrics.web.client.ObservationRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ObservationRestTemplateCustomizer customizer) {
        return new RestTemplateBuilder().customizers(customizer).build();
    }
}