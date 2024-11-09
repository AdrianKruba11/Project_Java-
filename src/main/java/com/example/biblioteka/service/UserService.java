package com.example.biblioteka.service;

import com.example.biblioteka.controller.dto.Userdto;
import com.example.biblioteka.model.User;
import com.example.biblioteka.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void registerUser(Userdto userdto) {
        if (!userdto.isPasswordConfirmed()) {
            throw new IllegalArgumentException("Hasła nie są zgodne");
        }

        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        userRepository.save(user);
    }



}
