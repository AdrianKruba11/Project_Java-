package com.example.biblioteka.repository;

import com.example.biblioteka.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
