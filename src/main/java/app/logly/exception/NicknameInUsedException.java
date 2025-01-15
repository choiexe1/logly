package app.logly.exception;

public class NicknameInUsedException extends RuntimeException {
    public static final String ERROR_CODE = "in.used.nickname";

    public NicknameInUsedException(String message) {
        super(message);
    }
}
