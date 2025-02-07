package com.example.biblioteka.service;

import com.example.biblioteka.model.Book;
import com.example.biblioteka.model.Rental;
import com.example.biblioteka.repository.BookRepository;
import com.example.biblioteka.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReturnService {

    @Autowired
    private final BookRepository bookRepository;
    private final RentalRepository rentalRepository;

    public void returnBook(int bookId, String returnDate) {
        // Parsowanie daty zwrotu
        LocalDate parsedReturnDate;
        try {
            parsedReturnDate = LocalDate.parse(returnDate, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Nieprawidłowy format daty: " + returnDate + ". Oczekiwano formatu YYYY-MM-DD.");
        }

        // Znalezienie wypożyczenia na podstawie ID książki
        Optional<Rental> rentalOpt = rentalRepository.findByBookId((long) bookId);
        if (rentalOpt.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono wypożyczenia dla podanego ID książki.");
        }

        Rental rental = rentalOpt.get();

        // Sprawdzenie, czy książka została już zwrócona
        if (rental.getReturnAt() != null) {
            throw new IllegalStateException("Książka została już zwrócona.");
        }

        // Pobranie książki z wypożyczenia
        Book book = rental.getBook();
        if (book == null) {
            throw new IllegalArgumentException("Nie znaleziono książki powiązanej z tym wypożyczeniem.");
        }

        // Zwiększenie liczby dostępnych egzemplarzy
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        // Aktualizacja daty zwrotu w wypożyczeniu
        rental.setReturnAt(parsedReturnDate.atStartOfDay());
        rentalRepository.save(rental);
    }


}
