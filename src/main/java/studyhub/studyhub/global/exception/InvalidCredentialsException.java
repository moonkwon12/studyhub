package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
    }
}

