package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class FriendRequestAlreadyPendingException extends BusinessException {
    public FriendRequestAlreadyPendingException() {
        super(HttpStatus.CONFLICT, "A friend request is already pending between users.");
    }
}

