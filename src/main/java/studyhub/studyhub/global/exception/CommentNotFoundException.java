package studyhub.studyhub.global.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends BusinessException {

  public CommentNotFoundException() {
    super(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");
  }
}