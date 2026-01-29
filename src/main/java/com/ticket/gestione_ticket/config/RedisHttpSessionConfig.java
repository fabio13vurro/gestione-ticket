package com.ticket.gestione_ticket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(
        redisNamespace = "gestioneticket:sessions",
        maxInactiveIntervalInSeconds = 1800 // 30 minuti
)
public class RedisHttpSessionConfig {
}