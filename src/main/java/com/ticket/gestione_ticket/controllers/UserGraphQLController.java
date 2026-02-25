package com.ticket.gestione_ticket.controllers;

import com.ticket.gestione_ticket.DTOs.UserExternalDTO;
import com.ticket.gestione_ticket.Models.AddressGraphQLModel;
import com.ticket.gestione_ticket.Models.UserGraphQLModel;
import com.ticket.gestione_ticket.config.HttpClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserGraphQLController {

    private final HttpClientProperties props;
    private final RestTemplate restTemplate = new RestTemplate();

    @QueryMapping
    public List<UserGraphQLModel> users() {
        UserExternalDTO[] externalUsers = restTemplate.getForObject(props.getUrl(), UserExternalDTO[].class);
        return Arrays.stream(externalUsers)
                .map(e -> new UserGraphQLModel(
                        e.getUsername(),
                        e.getEmail(),
                        new AddressGraphQLModel(
                                e.getAddress().getStreet(),
                                e.getAddress().getCity()
                        )
                        ))
                .toList();
    }
}