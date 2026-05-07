package com.ticket.gestione_ticket.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("currentPage", "home");
        return "home"; }
}
