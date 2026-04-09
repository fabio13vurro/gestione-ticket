package com.ticket.gestione_ticket.jobs;

import com.ticket.gestione_ticket.config.JobQueueConfig;
import com.ticket.gestione_ticket.entities.Ticket;
import com.ticket.gestione_ticket.repositories.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketSlaScheduler {

    private final TicketRepository ticketRepository;
    private final JobProducer jobProducer;

    public TicketSlaScheduler(TicketRepository ticketRepository, JobProducer jobProducer) {
        this.ticketRepository = ticketRepository;
        this.jobProducer = jobProducer;
    }

    //@Scheduled(fixedRateString = "${scheduler.check}")
    public void checkOverSlaTickets(){
        System.out.println("Controllo ticket over SLA");

        List<Ticket> lista = ticketRepository.findByOverSlaTrue();

        lista.forEach(ticket -> {
            String messaggio = "Ticket Id " + ticket.getIdTicket() + " è over SLA";
            jobProducer.sendJob(JobQueueConfig.JOB_QUEUE, messaggio);
            System.out.println("Job inviato per ticket " + ticket.getIdTicket() + " con messaggio: " + messaggio);
        });
    }
}