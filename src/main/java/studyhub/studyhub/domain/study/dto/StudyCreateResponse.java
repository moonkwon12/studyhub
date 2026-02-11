package studyhub.studyhub.domain.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "스터디 생성 응답")
public class StudyCreateResponse {

    @Schema(description = "생성된 스터디 ID", example = "1")
    private Long studyId;

    @Schema(description = "스터디 제목", example = "백엔드 스터디")
    private String title;
}