package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyJoinedStudyException extends BusinessException {
    public AlreadyJoinedStudyException() {
        super(HttpStatus.BAD_REQUEST, "이미 참여한 스터디입니다.");
    }
}