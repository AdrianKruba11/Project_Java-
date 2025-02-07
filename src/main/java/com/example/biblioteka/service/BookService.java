package com.example.biblioteka.service;

import com.example.biblioteka.model.Book;
import com.example.biblioteka.model.Rental;
import com.example.biblioteka.model.User;
import com.example.biblioteka.repository.BookRepository;
import com.example.biblioteka.repository.RentalRepository;
import com.example.biblioteka.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public boolean rentBook(Long bookId, String username) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            System.out.println("Book not found with ID: " + bookId);
            return false;
        }

        Book book = bookOptional.get();
        if (book.getAvailableQuantity() <= 0) {
            System.out.println("No available copies for book: " + book.getTitle());
            return false;
        }

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            System.out.println("User not found with username: " + username);
            return false;
        }

        User user = userOptional.get();
        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentedAt(LocalDateTime.now());

        rentalRepository.save(rental);
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        System.out.println("Book rented successfully: " + book.getTitle());
        return true;
    }

    public List<Rental> getReturnedBooksForUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        User user = userOpt.get();
        return rentalRepository.findReturnedBooksByUserId(user.getId());
    }

    public void addBook(String title, String author, String genre, int quantity) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setQuantity(quantity);
        book.setAvailableQuantity(quantity);
        bookRepository.save(book);
    }




    public class RentalService {

        public List<Rental> getActiveRentalsByUser(Long userId) {
            return rentalRepository.findActiveRentalsByUser(userId);
        }
    }

    public List<Rental> getActiveRentalsByUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        User user = userOpt.get();
        return rentalRepository.findActiveRentalsByUser(user.getId());
    }



}
