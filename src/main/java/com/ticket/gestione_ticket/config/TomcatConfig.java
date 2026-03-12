package com.ticket.gestione_ticket.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
    private static final int MAX_POST_SIZE = 120 * 1024 * 1024;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setMaxPostSize(MAX_POST_SIZE); //dimensione massima della request HTTP
            connector.setMaxSavePostSize(MAX_POST_SIZE); //dimensione buffer interno
            connector.setProperty("maxSwallowSize", String.valueOf(MAX_POST_SIZE)); //permette swallow di payload molto grandi
        });
    }
}