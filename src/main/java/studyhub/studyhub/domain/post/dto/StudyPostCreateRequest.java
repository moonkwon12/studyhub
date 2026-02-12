package studyhub.studyhub.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 생성 요청 DTO")
public record StudyPostCreateRequest(

        @Schema(description = "게시글 제목", example = "이번주 스터디 공지")
        String title,

        @Schema(description = "게시글 내용", example = "이번주는 JPA 트랜잭션을 다룹니다.")
        String content
) {}
