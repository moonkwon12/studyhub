package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "Authentication is required.");
    }
}

