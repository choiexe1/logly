package app.logly.exception;

public class EmailInUsedException extends RuntimeException {
    public static final String ERROR_CODE = "in.used.email";

    public EmailInUsedException(String message) {
        super(message);
    }
}
