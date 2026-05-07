package com.ticket.gestione_ticket.jobs;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobProducer {

    private final AmqpTemplate amqpTemplate;

    public JobProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendJob(String coda, String messaggio) {
        amqpTemplate.convertAndSend(coda, messaggio);
    }
}