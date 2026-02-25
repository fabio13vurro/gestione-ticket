package com.ticket.gestione_ticket.Models;

import lombok.Data;

@Data
public class AddressGraphQLModel {
    private String street;
    private String city;

    public AddressGraphQLModel(String street, String city){
        this.street = street;
        this.city = city;
    }
}