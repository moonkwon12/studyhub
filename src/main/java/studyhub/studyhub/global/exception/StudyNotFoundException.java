package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class StudyNotFoundException extends BusinessException {
    public StudyNotFoundException() {
        super(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다.");
    }
}