package com.ticket.gestione_ticket.auth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private Map<String, String> tokens = new ConcurrentHashMap<>();

    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, username);
        return token;
    }

    public boolean isValidToken(String token) {
        return tokens.containsKey(token);
    }

    public String getUsernameFromToken(String token) {
        return tokens.get(token);
    }
}
