package pl.dskimina.foodsy.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){super(message);}
}
