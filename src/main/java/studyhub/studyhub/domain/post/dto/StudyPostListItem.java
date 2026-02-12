package studyhub.studyhub.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게시글 목록 응답 DTO")
public record StudyPostListItem(

        @Schema(description = "게시글 ID", example = "1")
        Long postId,

        @Schema(description = "게시글 제목", example = "공지사항")
        String title,

        @Schema(description = "작성자 이름", example = "홍길동")
        String authorName,

        @Schema(description = "댓글 개수", example = "3")
        long commentCount,

        @Schema(description = "작성 시각")
        LocalDateTime createdAt
) {}
