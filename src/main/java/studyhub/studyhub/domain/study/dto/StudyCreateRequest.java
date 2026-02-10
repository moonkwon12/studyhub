package studyhub.studyhub.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "스터디 생성 요청 DTO")
@Getter
public class StudyCreateRequest {

    @Schema(description = "스터디 생성자 사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "스터디 제목", example = "JPA 스터디")
    private String title;

    @Schema(description = "스터디 설명", example = "Spring Data JPA 학습 스터디")
    private String description;
}
