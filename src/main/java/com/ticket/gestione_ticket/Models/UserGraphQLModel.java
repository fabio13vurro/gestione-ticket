package com.ticket.gestione_ticket.Models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class UserGraphQLModel {
    private String username;
    private String email;
    private AddressGraphQLModel address;

    public UserGraphQLModel(String username, String email, AddressGraphQLModel address){
        this.username = username;
        this.email = email;
        this.address = address;
    }
}