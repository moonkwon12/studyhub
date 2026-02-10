package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class StudyMemberNotFoundException extends BusinessException {
    public StudyMemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, "스터디 참여 정보가 없습니다.");
    }
}