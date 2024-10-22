package com.example.biblioteka.controller;

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
    public User registerNewUser(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String role) {
        return userService.registerNewUser(username, password, role);
    }
}
