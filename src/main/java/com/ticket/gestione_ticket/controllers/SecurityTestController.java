
package com.ticket.gestione_ticket.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "basicAuth")
public class SecurityTestController {

    @GetMapping("/api/admin/test")
    public String adminTest() {
        return "Accesso admin riuscito";
    }

    @GetMapping("/api/operatore/test")
    public String operatoreTest() {
        return "Accesso operatore riuscito";
    }

    @GetMapping("/api/cliente/test")
    public String clienteTest() {
        return "Accesso cliente riuscito";
    }
}
