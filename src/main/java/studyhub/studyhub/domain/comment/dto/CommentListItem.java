package studyhub.studyhub.domain.comment.dto;

import java.time.LocalDateTime;

public record CommentListItem(
        Long commentId,
        Long authorId,
        String authorName,
        String content,
        LocalDateTime createdAt,
        boolean mine,
        boolean canEdit,
        boolean canDelete
) {
}
