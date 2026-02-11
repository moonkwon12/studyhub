package studyhub.studyhub.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Schema(description = "스터디 생성 요청 DTO")
@Getter
public class StudyCreateRequest {

    @NotNull(message = "사용자 ID는 필수입니다.")
    @Schema(description = "스터디 생성자 사용자 ID", example = "1")
    private Long userId;

    @NotBlank(message = "스터디 제목은 필수입니다.")
    @Size(max = 50, message = "스터디 제목은 50자 이하로 입력하세요.")
    @Schema(description = "스터디 제목", example = "JPA 스터디")
    private String title;

    @NotBlank(message = "스터디 설명은 필수입니다.")
    @Size(max = 200, message = "스터디 설명은 200자 이하로 입력하세요.")
    @Schema(description = "스터디 설명", example = "Spring Data JPA 학습 스터디")
    private String description;
}