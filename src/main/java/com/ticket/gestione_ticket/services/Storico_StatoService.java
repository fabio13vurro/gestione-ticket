package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Storico_Stato;
import com.ticket.gestione_ticket.repositories.Storico_StatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Storico_StatoService {

    private final Storico_StatoRepository storico_statoRepository;

    public Storico_StatoService(Storico_StatoRepository storico_statoRepository) {
        this.storico_statoRepository = storico_statoRepository;
    }

    public Storico_Stato create(Storico_Stato storico_stato){
        return storico_statoRepository.save(storico_stato);
    }

    public void deleteById(int id){
        Storico_Stato storico_stato = storico_statoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico_Stato non trovato: " + id));

        storico_stato.setDeleted(true);
        storico_statoRepository.save(storico_stato);
    }

    @Transactional
    public Storico_Stato update(Integer id, Storico_Stato new_storico_stato){
        Storico_Stato storico_stato = storico_statoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico_Stato non trovato: " + id));

        if(new_storico_stato.getStato_precedente() != null) storico_stato.setStato_precedente(new_storico_stato.getStato_precedente());
        if(new_storico_stato.getStato_nuovo() != null) storico_stato.setStato_nuovo(new_storico_stato.getStato_nuovo());
        if(new_storico_stato.getData_ora() != null) storico_stato.setData_ora(new_storico_stato.getData_ora());
        return storico_statoRepository.save(storico_stato);
    }

    public Storico_Stato findById(Integer id){
        return storico_statoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico_Stato non trovato: " + id));
    }

    public List<Storico_Stato> findAll(){
        return storico_statoRepository.findAll();
    }
}