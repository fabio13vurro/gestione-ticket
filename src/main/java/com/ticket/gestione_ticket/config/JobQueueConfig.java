package com.ticket.gestione_ticket.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobQueueConfig {

    public static final String JOB_QUEUE = "ticket.jobs.queue";

    @Bean
    public Queue jobQueue() {
        return new Queue(JOB_QUEUE, true);
    }
}