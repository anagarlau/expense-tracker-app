package de.htwberlin.webtech.expensetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value={"", "/", "home"})
    public String displayHome(Model model){
        String greeting = "Hello World";
        model.addAttribute("greeting", greeting);
        return "helloworld";
    }
}
