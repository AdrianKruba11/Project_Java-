package com.example.biblioteka.controller.thymeleaf;

import com.example.biblioteka.controller.dto.UserDto;
import com.example.biblioteka.exception.UserNotFoundException;
import com.example.biblioteka.service.BookService;
import com.example.biblioteka.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final BookService bookService;
    private final UserService userService;


    @GetMapping({"/", "/index"})
    public String index(Model model, HttpSession session) {
        model.addAttribute("userdto", new UserDto());
        model.addAttribute("books", bookService.findAllBooks());

        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", session.getAttribute("role"));
        }

        model.addAttribute("books", bookService.findAllBooks());
        return "index";
    }

    @GetMapping("/userpanel")
    public String userPanel(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Redirect to login if no user is logged in
        }

        model.addAttribute("username", username);
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("books", bookService.findAllBooks());
        model.addAttribute("rentals", bookService.getActiveRentalsByUser(username)); // Fetch active rentals

        return "userpanel"; // Render userpanel.html
    }






    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("userdto") UserDto userdto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("result", "Walidacja danych nie powiodła się.");
            log.warn("Błędy walidacji : {}", bindingResult.getAllErrors());
            return "register";
        }

        if (!confirmPassword(userdto)) {
            model.addAttribute("result", "Hasła nie pasują do siebie.");
            log.warn("Hasła nie pasują do siebie dla użytkownika: {}", userdto.getUsername());
            return "register";
        }

        if (!isValidPassword(userdto.getPassword())) {
            model.addAttribute("result", "Hasło nie spełnia wymagań bezpieczeństwa.");
            log.warn("Hasło użytkownika [{}] nie spełnia wymagań bezpieczeństwa.", userdto.getUsername());
            return "register";
        }

        if (userService.isUsernameTaken(userdto.getUsername())) {
            model.addAttribute("result", "Nazwa użytkownika jest już zajęta.");
            log.warn("Nazwa użytkownika [{}] jest już zajęta.", userdto.getUsername());
            return "register";
        }

        if (userService.isEmailTaken(userdto.getEmail())) {
            model.addAttribute("result", "E-mail jest już zarejestrowany.");
            log.warn("E-mail [{}] jest już zarejestrowany.", userdto.getEmail());
            return "register";
        }

        userService.registerNewUser(userdto.getUsername(), userdto.getPassword(), userdto.getEmail());

        log.info("Użytkownik [{}] zarejestrował się pomyślnie.", userdto.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam String login, @RequestParam String password, HttpSession session) {
        boolean isAuthenticated = false;

        if (isEmail(login)) {
            isAuthenticated = userService.authenticateUserByEmail(login, password);
        }
        else {
            isAuthenticated = userService.authenticateUserByUsername(login, password);
        }

        if (isAuthenticated) {
            String role = userService.getUsersRole(login);
            session.setAttribute("username", login);
            session.setAttribute("role", role);
            model.addAttribute("result", "Zalogowano pomyślnie.");
        }
        else {
            model.addAttribute("result", "Nieprawidłowy login lub hasło");
        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Usunięcie danych z sesji
        return "redirect:/";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model, @RequestParam String username, @RequestParam String currentPassword,
                                 @RequestParam String newPassword, @RequestParam String confirmPassword, HttpSession session) {
        log.info("Attempting to change password for user: {}", username);  // Add this log

        String sessionUsername = (String) session.getAttribute("username");
        if (sessionUsername == null || !sessionUsername.equals(username)) {
            model.addAttribute("result", "Musisz być zalogowany, aby zmienić hasło.");
            return "index";
        }

        if (!isValidPassword(newPassword)) {
            model.addAttribute("result", "Nowe hasło nie spełnia wymagań bezpieczeństwa.");
            return "index";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("result", "Nowe hasła nie pasują do siebie.");
            return "index";
        }

        try {
            log.info("Calling userService.changePassword()...");  // Log this line to ensure method is called
            userService.changePassword(username, newPassword);
            model.addAttribute("result", "Hasło zostało zmienione");
            log.info("Użytkownik [{}] zmienił hasło.", username);
        } catch (UserNotFoundException e) {
            model.addAttribute("result", "Błąd: " + e.getMessage());
            log.error("Nie udało się zmienić hasła dla użytkownika [{}] : {}", username, e.getMessage());
        }
        return "index";
    }


    @PostMapping("/delete-user")
    public String deleteUser(Model model, String userid) {
        try {
            userService.deleteUser(userid); // Ensure this method deletes the user based on ID
            model.addAttribute("result", "Użytkownik został usunięty");
        } catch (UserNotFoundException e) {
            model.addAttribute("result", "Błąd: Użytkownik nie został znaleziony");
        } catch (Exception e) {
            model.addAttribute("result", "Błąd podczas usuwania użytkownika: " + e.getMessage());
        }
        return "/adminpanel"; // Replace with the name of your admin panel view
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[0-9].*") && password.matches(".*[a-zA-Z].*");
    }

    private boolean confirmPassword(UserDto userdto) {
        return userdto.getPassword()
                .equals(userdto.getConfirmPassword());
    }

    private boolean isEmail(String login) {
        return login != null && login.contains("@");
    }
}
