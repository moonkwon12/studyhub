package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class CannotFriendYourselfException extends BusinessException {
    public CannotFriendYourselfException() {
        super(HttpStatus.BAD_REQUEST, "You cannot send a friend request to yourself.");
    }
}

