package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends BusinessException {
    public ChatRoomNotFoundException() {
        super(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다.");
    }
}
