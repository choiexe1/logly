package app.logly.exception;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    public static final String ERROR_CODE = "password.invalid";

    public InvalidPasswordException(String message) {
        super(message);
    }
}
