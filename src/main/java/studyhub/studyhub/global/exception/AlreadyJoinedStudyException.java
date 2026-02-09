package studyhub.studyhub.global.exception;

public class AlreadyJoinedStudyException extends BusinessException{
    public AlreadyJoinedStudyException() {
        super("이미 참여한 스터디입니다.");
    }
}
