package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class FriendRequestNotFoundException extends BusinessException {
    public FriendRequestNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Friend request not found.");
    }
}

