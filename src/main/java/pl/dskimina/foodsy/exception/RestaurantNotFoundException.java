package pl.dskimina.foodsy.exception;

public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException(String message){super(message);}
}
