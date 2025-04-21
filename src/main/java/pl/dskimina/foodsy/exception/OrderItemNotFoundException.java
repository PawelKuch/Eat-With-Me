package pl.dskimina.foodsy.exception;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(String message) {super(message);}
}
