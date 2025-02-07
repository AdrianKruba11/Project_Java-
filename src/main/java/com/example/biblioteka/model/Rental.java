package com.example.biblioteka.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime rentedAt; // Data wypożyczenia

    private LocalDateTime returnAt; // Data zwrotu

    // Konstruktor
    public Rental(Book book, User user, LocalDate rentalDate, LocalDate returnDate) {
        this.book = book;
        this.user = user;
        this.rentedAt = rentalDate.atStartOfDay(); // Konwersja LocalDate -> LocalDateTime
        this.returnAt = returnDate.atStartOfDay(); // Konwersja LocalDate -> LocalDateTime
    }
}

