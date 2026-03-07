package studyhub.studyhub.domain.study.dto;

import java.time.LocalDateTime;

public record StudyDetailResponse(
        Long studyId,
        String title,
        String description,
        LocalDateTime createdAt
) {
}

