package com.example.biblioteka.repository;

import com.example.biblioteka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
