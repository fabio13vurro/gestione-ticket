package com.ticket.gestione_ticket.DTOs;

import lombok.Data;

@Data
public class UserExternalDTO {
    private String username;
    private String email;
    private AddressExternalDTO address;
}