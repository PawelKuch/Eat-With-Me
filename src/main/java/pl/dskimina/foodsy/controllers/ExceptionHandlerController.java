package pl.dskimina.foodsy.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.dskimina.foodsy.exception.*;

@ControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderItemNotFoundException(OrderNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errorException";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errorException";
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public String handleRestaurantNotFoundException(RestaurantNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errorException";
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public String handleOrderItemNotFoundException(OrderItemNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errorException";
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    public String handleMenuItemNotFoundException(MenuItemNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errorException";
    }

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(BadRequestException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errorException";
    }
}
