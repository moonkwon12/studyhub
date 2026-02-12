package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class StudyMemberNotFoundException extends BusinessException {
    public StudyMemberNotFoundException() {
        super(HttpStatus.FORBIDDEN, "스터디 멤버가 아닙니다.");
    }
}