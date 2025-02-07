package com.example.biblioteka.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Obsługa UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("result", "Błąd: " + ex.getMessage());
        return modelAndView;
    }

    // Obsługa IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("result", "Niepoprawne dane: " + ex.getMessage());
        return modelAndView;
    }

    // Obsługa ogólnych wyjątków
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("result", "Wystąpił błąd: " + ex.getMessage());
        return modelAndView;
    }
}
