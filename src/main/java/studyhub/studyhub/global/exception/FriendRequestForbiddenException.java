package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class FriendRequestForbiddenException extends BusinessException {
    public FriendRequestForbiddenException() {
        super(HttpStatus.FORBIDDEN, "You do not have permission to handle this friend request.");
    }
}

