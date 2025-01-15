package app.logly.exception;

public class UserNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "user.not.exist";

    public UserNotFoundException(String message) {
        super(message);
    }
}
