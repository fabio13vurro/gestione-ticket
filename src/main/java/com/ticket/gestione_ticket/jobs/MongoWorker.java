package com.ticket.gestione_ticket.jobs;

import org.springframework.dao.DuplicateKeyException;
import com.ticket.gestione_ticket.config.JobQueueConfig;
import com.ticket.gestione_ticket.mongodb.documents.OverSlaTicketDocument;
import com.ticket.gestione_ticket.mongodb.repositories.OverSlaTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class MongoWorker {
    private final OverSlaTicketRepository mongoRepository;
    private final ObjectMapper objectMapper;
    private final JobProducer jobProducer;

    @RabbitListener(queues = JobQueueConfig.MONGO_QUEUE)
    public void processSnapshot(String json) {
        System.out.println("Snapshot ricevuto da RabbitMQ: " + json);
        try {
            OverSlaTicketDocument doc = objectMapper.readValue(json, OverSlaTicketDocument.class);
            mongoRepository.save(doc);
            System.out.println("Snapshot salvato su MongoDB");
        }catch (DuplicateKeyException dup){
            System.out.println("Documento già esistente");
            return;
        } catch (Exception e){
            System.out.println("Errore nel salvataggio dello snapshot su MongoDB");
            throw e;
        }
    }
}