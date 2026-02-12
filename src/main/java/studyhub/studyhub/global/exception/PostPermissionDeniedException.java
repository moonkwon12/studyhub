package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class PostPermissionDeniedException extends BusinessException {

    public PostPermissionDeniedException() {
        super(HttpStatus.FORBIDDEN, "게시글에 대한 권한이 없습니다.");
    }
}
