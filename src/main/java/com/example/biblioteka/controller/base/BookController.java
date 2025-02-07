package com.example.biblioteka.controller.base;

import org.springframework.ui.Model;
import com.example.biblioteka.model.Book;
import com.example.biblioteka.model.Rental;
import com.example.biblioteka.service.BookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/rent")
    public String rentBook(@RequestParam("bookId") Long bookId, HttpSession session) {
        try {
            String username = (String) session.getAttribute("username");
            if (username == null) {
                return "redirect:/login"; // Redirect to login page if not logged in
            }

            boolean isRented = bookService.rentBook(bookId, username);
            if (isRented) {
                return "redirect:/index"; // Redirect to book list
            } else {
                return "error"; // Display error if rental failed
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Redirect to an error page on exception
        }
    }

    @GetMapping("/returnedbooks")
    public String showReturnedBooks(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Jeśli użytkownik nie jest zalogowany, przekieruj na stronę logowania
        }

        List<Rental> returnedBooks = bookService.getReturnedBooksForUser(username);
        model.addAttribute("returnedBooks", returnedBooks);

        return "userpanel"; // Nazwa szablonu HTML (returnedbooks.html)
    }





}

