package com.ticket.gestione_ticket.repositories;

import com.ticket.gestione_ticket.entities.Ruolo;
import com.ticket.gestione_ticket.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Utente findByUsername(String username);

    List<Utente> findByRuolo(Ruolo ruolo);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Utente u WHERE " +
            "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:ruolo IS NULL OR LOWER(u.ruolo) = LOWER(:ruolo)) AND" +
            "(:ticketAssegnati IS NULL OR CAST(u.ticketAssegnati as string) = :ticketAssegnati) AND " +
            "(:address IS NULL OR LOWER(u.address) LIKE LOWER(CONCAT('%', :address, '%')))")
    Page<Utente> filtraUtenti(@Param("username") String username,
                              @Param("email") String email,
                              @Param("ruolo") String ruolo,
                              @Param("ticketAssegnati") String ticketAssegnati,
                              @Param("address") String address,
                              Pageable pageable);
}