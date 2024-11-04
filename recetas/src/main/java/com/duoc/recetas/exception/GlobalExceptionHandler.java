package com.duoc.recetas.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletResponse response, Model model) {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        model.addAttribute("errorMessage", "Ocurrió un error inesperado. Inténtalo nuevamente más tarde.");
        return "error-page"; 
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, HttpServletResponse response, Model model) {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        model.addAttribute("errorMessage", "Página no encontrada.");
        return "error-page"; 
    }

}
