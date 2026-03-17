package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.StoricoStato;
import com.ticket.gestione_ticket.repositories.StoricoStatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoricoStatoService {

    private final StoricoStatoRepository storicoStatoRepository;

    public StoricoStatoService(StoricoStatoRepository storicoStatoRepository) {
        this.storicoStatoRepository = storicoStatoRepository;
    }

    public StoricoStato create(StoricoStato storicoStato){
        return storicoStatoRepository.save(storicoStato);
    }

    public void deleteById(int id){
        StoricoStato storicoStato = storicoStatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico stato non trovato: " + id));

        storicoStato.setDeleted(true);
        storicoStatoRepository.save(storicoStato);
    }

    public void ripristina(Integer id){
        StoricoStato storicoStato = storicoStatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico stato non trovato: " + id));

        storicoStato.setDeleted(false);
        storicoStatoRepository.save(storicoStato);
    }

    @Transactional
    public StoricoStato update(Integer id, StoricoStato new_storicoStato){
        StoricoStato storicoStato = storicoStatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico stato non trovato: " + id));

        if(new_storicoStato.getStato_precedente() != null) storicoStato.setStato_precedente(new_storicoStato.getStato_precedente());
        if(new_storicoStato.getStato_nuovo() != null) storicoStato.setStato_nuovo(new_storicoStato.getStato_nuovo());
        if(new_storicoStato.getData_ora() != null) storicoStato.setData_ora(new_storicoStato.getData_ora());
        return storicoStatoRepository.save(storicoStato);
    }

    public StoricoStato findById(Integer id){
        return storicoStatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storico stato non trovato: " + id));
    }

    public List<StoricoStato> findAll(){
        return storicoStatoRepository.findAll();
    }
}