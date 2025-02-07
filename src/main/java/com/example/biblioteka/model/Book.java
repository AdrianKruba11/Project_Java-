package com.example.biblioteka.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "books")
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;

    private int quantity;
    private int availableQuantity;

    @OneToMany(mappedBy = "book")
    private Collection<Rental> rental;


    public Book(String title, String author, String genre, int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.availableQuantity = quantity;
    }

    private boolean rented;

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

}
