package com.ticket.gestione_ticket.config;

// package com.ticket.gestione_ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class GraphqlSecurityConfig {

    @Bean
    @Order(0)
    SecurityFilterChain graphqlChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/graphql", "/graphiql", "/graphiql/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/graphql", "/graphiql", "/graphiql/**").permitAll()
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}