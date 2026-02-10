package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class LeaderCannotLeaveException extends BusinessException {
    public LeaderCannotLeaveException() {
        super(HttpStatus.BAD_REQUEST, "스터디장은 탈퇴할 수 없습니다.");
    }
}
