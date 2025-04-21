package pl.dskimina.foodsy.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message){super(message);}
}
