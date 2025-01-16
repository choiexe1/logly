package app.logly.exception;

public class VerificationCodeNotMatchException extends RuntimeException {
    public static final String ERROR_CODE = "not.match.verify";

    public VerificationCodeNotMatchException(String message) {
        super(message);
    }
}
