package com.example.biblioteka.controller.info;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {

    @GetMapping("/stats")
    public String getStats(Model model) {
        model.addAttribute("activeUsers", 42);
        model.addAttribute("borrowedBooks", 42);
        return "stats";
    }
}
