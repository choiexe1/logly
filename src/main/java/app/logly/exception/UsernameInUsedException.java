package app.logly.exception;

public class UsernameInUsedException extends RuntimeException {
    public static final String ERROR_CODE = "in.used.username";

    public UsernameInUsedException(String message) {
        super(message);
    }
}
