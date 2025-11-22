package com.bookshop.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle ResourceNotFoundException
     * Returns 404 error page
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", "Resource Not Found");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 404);
        return "error/404";
    }
    
    /**
     * Handle InsufficientStockException
     * Returns error message with stock information
     */
    @ExceptionHandler(InsufficientStockException.class)
    public String handleInsufficientStockException(InsufficientStockException ex, 
                                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorType", "stock");
        
        if (ex.getBookId() != null) {
            return "redirect:/books/" + ex.getBookId();
        }
        return "redirect:/cart";
    }
    
    /**
     * Handle DuplicateUserException
     * Returns registration form with error
     */
    @ExceptionHandler(DuplicateUserException.class)
    public String handleDuplicateUserException(DuplicateUserException ex, 
                                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorType", "duplicate");
        return "redirect:/auth/register";
    }
    
    /**
     * Handle InvalidPaymentException
     * Returns payment form with error
     */
    @ExceptionHandler(InvalidPaymentException.class)
    public String handleInvalidPaymentException(InvalidPaymentException ex, 
                                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorType", "payment");
        return "redirect:/payment";
    }
    
    /**
     * Handle UnauthorizedException
     * Redirects to login page
     */
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException ex, 
                                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/auth/login";
    }
    
    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        model.addAttribute("validationErrors", errors);
        model.addAttribute("error", "Validation Failed");
        model.addAttribute("message", "Please correct the errors and try again");
        return "error/validation";
    }
    
    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception ex, Model model) {
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "An unexpected error occurred: " + ex.getMessage());
        model.addAttribute("status", 500);
        return "error/500";
    }
}
