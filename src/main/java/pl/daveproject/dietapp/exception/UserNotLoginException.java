package pl.daveproject.dietapp.exception;

public class UserNotLoginException extends RuntimeException {
    public UserNotLoginException() {
        super("User not logged, please login first to the system");
    }
}
