package com.example.biblioteka.controller.thymeleaf;

import com.example.biblioteka.controller.dto.Userdto;
import com.example.biblioteka.service.BookService;
import com.example.biblioteka.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final BookService bookService;
    private final UserService userService;

    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("userdto", new Userdto());
        model.addAttribute("requestCount","Paczej szwajgier");
        model.addAttribute("books", bookService.findAllBooks());
        return "index";
    }

    @PostMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userdto", new Userdto());
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    private boolean confirmPassword(Userdto userdto) {
        return userdto.getPassword().equals(userdto.getConfirmPassword());
    }
}

