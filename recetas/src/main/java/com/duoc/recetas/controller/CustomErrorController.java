package com.duoc.recetas.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        
        String errorMessage;
        if (statusCode == 404) {
            errorMessage = "Página no encontrada";
        } else if (statusCode == 500) {
            errorMessage = "Error interno del servidor";
        } else {
            errorMessage = "Ocurrió un error inesperado";
        }
        
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("statusCode", statusCode);
        return "error-page"; 
    }

    public String getErrorPath() {
        return "/error";
    }

}
