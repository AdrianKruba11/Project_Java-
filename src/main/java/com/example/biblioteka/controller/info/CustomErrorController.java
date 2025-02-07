package com.example.biblioteka.controller.info;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        WebRequest webRequest = new ServletWebRequest(request);

        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        );

        Integer status = (Integer) errorDetails.get("status");
        String error = (String) errorDetails.get("error");
        String message = (String) errorDetails.get("message");

        model.addAttribute("status", status);
        model.addAttribute("error", error);
        model.addAttribute("message", message);

        if (status != null && status == 404) {
            model.addAttribute("message", "Przepraszamy, strona nie została znaleziona.");
        }

        // Zwracanie widoku Thymeleaf
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}

























