package com.example.biblioteka.controller.base;

import com.example.biblioteka.service.BookService;
import com.example.biblioteka.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final BookService bookService;
    private final ReturnService returnService;

    @GetMapping("/adminpanel")
    public String adminpanel(Model model) {
        return "adminpanel";
    }

    @PostMapping("/addbooks")
    public String addbooks(@RequestParam String title, @RequestParam String author, @RequestParam String genre,
                           @RequestParam @Min(1) int quantity, Model model) {
        bookService.addBook(title, author, genre, quantity);
        model.addAttribute("result", "Książka została dodana do katalogu.");
        return "redirect:/adminpanel";
    }


    @PostMapping("/returnedbooks")
    public String returnedbooks(@RequestParam @Min(1) int bookId,
                                @RequestParam @NotBlank String returnDate,
                                Model model) {
        try {
            log.info("Otrzymano żądanie zwrotu książki. ID: {}, Data zwrotu: {}", bookId, returnDate);
            returnService.returnBook(bookId, returnDate);
            model.addAttribute("result", "Zwrot książki został pomyślnie zarejestrowany.");
        } catch (IllegalArgumentException e) {
            log.error("Błąd: " + e.getMessage(), e);
            model.addAttribute("result", "Błąd: " + e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Błąd: " + e.getMessage(), e);
            model.addAttribute("result", "Błąd: " + e.getMessage());
        } catch (Exception e) {
            log.error("Nieoczekiwany błąd: " + e.getMessage(), e);
            model.addAttribute("result", "Nieoczekiwany błąd: " + e.getMessage());
        }
        return "redirect:/adminpanel";
    }




}
