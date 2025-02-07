package com.example.biblioteka.controller.info;


import com.example.biblioteka.exception.UserNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
        model.addAttribute("result","Błąd:" + ex.getMessage());
        return "index";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("result", "Nieprawidłowe dane: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("result", "Wystąpił nieoczekiwany błąd: " + ex.getMessage());
        return "error";
    }
}
