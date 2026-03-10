package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class ChatOnlyForFriendsException extends BusinessException {
    public ChatOnlyForFriendsException() {
        super(HttpStatus.BAD_REQUEST, "친구 상태인 사용자와만 채팅할 수 있습니다.");
    }
}
