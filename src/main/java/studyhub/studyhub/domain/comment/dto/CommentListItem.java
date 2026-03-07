package studyhub.studyhub.domain.comment.dto;

import java.time.LocalDateTime;

public record CommentListItem(
        Long commentId,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
}

