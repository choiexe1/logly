package app.logly.exception;

public class UserNotFoundException extends RuntimeException {
    public static final String ERROR_CODE = "not.exist.user";

    public UserNotFoundException(String message) {
        super(message);
    }
}
