package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.config.JobQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class JobWorker {

    //@RabbitListener(queues = JobQueueConfig.JOB_QUEUE)
    public void processJob(String messaggio) {
        System.out.println("Job ricevuto: " + messaggio);
    }
}
