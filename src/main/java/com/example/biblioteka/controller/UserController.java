package com.example.biblioteka.controller;

import com.example.biblioteka.controller.dto.Userdto;
import com.example.biblioteka.model.User;
import com.example.biblioteka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Userdto userdto) {
        userService.registerUser(userdto);
        return "redirect:/login";
    }
}
