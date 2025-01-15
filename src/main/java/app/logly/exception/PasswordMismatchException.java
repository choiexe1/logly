package app.logly.exception;

public class PasswordMismatchException extends RuntimeException {
    public static final String ERROR_CODE = "password.mismatch";

    public PasswordMismatchException(String message) {
        super(message);
    }
}
