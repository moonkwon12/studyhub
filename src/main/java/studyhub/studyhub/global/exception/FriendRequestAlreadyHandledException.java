package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class FriendRequestAlreadyHandledException extends BusinessException {
    public FriendRequestAlreadyHandledException() {
        super(HttpStatus.BAD_REQUEST, "This friend request has already been handled.");
    }
}

