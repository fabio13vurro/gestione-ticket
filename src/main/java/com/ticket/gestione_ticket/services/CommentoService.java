package com.ticket.gestione_ticket.services;

import com.ticket.gestione_ticket.entities.Commento;
import com.ticket.gestione_ticket.repositories.CommentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;

    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }

    public Commento create(Commento commento){
        return commentoRepository.save(commento);
    }

    public void deleteById(int id){
        commentoRepository.deleteById(id);
    }

    @Transactional
    public Commento update(Integer id, Commento new_commento){

        Commento commento = commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato: " + id));

        if(new_commento.getTesto() != null) commento.setTesto(new_commento.getTesto());
        if (new_commento.getTipo() != null) commento.setTipo(new_commento.getTipo());
        if (new_commento.getData_ora() != null) commento.setData_ora(new_commento.getData_ora());
        return commentoRepository.save(commento);
    }

    public List<Commento> findAll(){
        return commentoRepository.findAll();
    }

    public Commento findById(Integer id){
        return commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato: " + id));
    }

    public Commento findByTipo(String tipo){
        return commentoRepository.findByTipo(tipo);
    }
}