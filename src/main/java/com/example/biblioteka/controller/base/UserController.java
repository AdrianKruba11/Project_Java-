package com.example.biblioteka.controller.base;

import com.example.biblioteka.model.User;
import com.example.biblioteka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Rejestracja nowego użytkownika
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestParam String username,
                                             @RequestParam String password,
                                             @RequestParam String email) {
        if (userService.isUsernameTaken(username)) {
            return ResponseEntity.badRequest().body("Nazwa użytkownika jest już zajęta.");
        }
        if (userService.isEmailTaken(email)) {
            return ResponseEntity.badRequest().body("E-mail jest już używany.");
        }

        try {
            userService.registerNewUser(username, password, email);
            return ResponseEntity.status(302)
                    .header("Location", "/index.html") // Przekierowanie po pomyślnej rejestracji
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Wystąpił błąd podczas rejestracji użytkownika.");
        }
    }




    // Logowanie użytkownika
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String login, @RequestParam String password) {
        boolean isAuthenticated = login.contains("@")
                ? userService.authenticateUserByEmail(login, password)
                : userService.authenticateUserByUsername(login, password);

        if (isAuthenticated) {
            return ResponseEntity.status(302)
                    .header("Location", "/index.html")
                    .build();
        } else {
            return ResponseEntity.status(401).body("Nieprawidłowy login lub hasło.");
        }
    }






}
