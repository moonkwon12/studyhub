package studyhub.studyhub.global.exception;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
