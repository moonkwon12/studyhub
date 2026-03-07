package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyFriendsException extends BusinessException {
    public AlreadyFriendsException() {
        super(HttpStatus.CONFLICT, "You are already friends.");
    }
}

