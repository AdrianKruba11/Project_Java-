package com.example.biblioteka.service;


import com.example.biblioteka.controller.base.UserController;
import com.example.biblioteka.exception.UserAlreadyExistsExeption;
import com.example.biblioteka.exception.UserNotFoundException;
import com.example.biblioteka.model.User;
import com.example.biblioteka.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User registerNewUser(String username, String password, String email) {
        System.out.println("Rejestracja użytkownika: " + username + ", email: " + email);

        if (userRepository.findByUsername(username).isPresent()) {
            System.out.println("Nazwa użytkownika jest już zajęta: " + username);
            throw new IllegalArgumentException("Nazwa użytkownika jest już zajęta");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            System.out.println("E-mail jest już przypisany do konta: " + email);
            throw new UserAlreadyExistsExeption("Do tego e-maila jest już przypisane konto");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("USER");

        User savedUser = userRepository.save(user);
        System.out.println("Zarejestrowano użytkownika: " + savedUser.getUsername() + " (ID: " + savedUser.getId() + ")");
        return savedUser;
    }




    public void changePassword(String username, String newPassword) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword)); // Assuming you're using bcrypt or similar
        userRepository.save(user);  // Persist the updated user
    }


    public void deleteUser(String userid) {
        Optional<User> user = userRepository.findById(Long.valueOf(userid));
        if (user.isPresent()) {
            userRepository.delete(user.get());
            System.out.println("Deleted user with ID: " + userid); // Debug log
        } else {
            throw new UserNotFoundException("User with ID " + userid + " not found.");
        }
    }




    public boolean authenticateUserByUsername(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public boolean authenticateUserByEmail(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }


    public String getUsersRole(String login) {
        return login.contains("@")
                ? userRepository.findByEmail(login).map(User::getRole).orElse(null)
                : userRepository.findByUsername(login).map(User::getRole).orElse(null);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }






}
