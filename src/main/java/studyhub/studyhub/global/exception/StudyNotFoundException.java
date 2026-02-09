package studyhub.studyhub.global.exception;

public class StudyNotFoundException extends BusinessException {
    public StudyNotFoundException() {
        super("스터디를 찾을 수 없습니다.");
    }
}
