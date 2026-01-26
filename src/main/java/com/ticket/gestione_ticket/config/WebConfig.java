package com.ticket.gestione_ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public org.springframework.web.filter.HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new org.springframework.web.filter.HiddenHttpMethodFilter();
    }
}