package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class ChatAccessDeniedException extends BusinessException {
    public ChatAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, "해당 채팅방에 접근할 수 없습니다.");
    }
}
