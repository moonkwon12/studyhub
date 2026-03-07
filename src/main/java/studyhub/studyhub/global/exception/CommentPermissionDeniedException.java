package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class CommentPermissionDeniedException extends BusinessException {

    public CommentPermissionDeniedException() {
        super(HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.");
    }
}

