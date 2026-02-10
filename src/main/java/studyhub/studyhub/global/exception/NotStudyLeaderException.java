package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class NotStudyLeaderException extends BusinessException {
    public NotStudyLeaderException() {
        super(HttpStatus.FORBIDDEN, "리더만 권한을 위임할 수 있습니다.");
    }
}