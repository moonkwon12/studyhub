package studyhub.studyhub.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게시글 생성 응답 DTO")
public record StudyPostCreateResponse(

        @Schema(description = "게시글 ID", example = "5")
        Long postId,

        @Schema(description = "생성 시각")
        LocalDateTime createdAt
) {}
