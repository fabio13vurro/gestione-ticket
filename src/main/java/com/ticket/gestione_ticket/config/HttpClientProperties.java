package com.ticket.gestione_ticket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.client")
public class HttpClientProperties {

    private int connectTimeout;
    private int readTimeout;
    private int retryBackoff;
    private int maxAttempts;
    private String url;
}