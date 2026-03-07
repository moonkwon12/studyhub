package studyhub.studyhub.domain.study.dto;

import studyhub.studyhub.domain.studymember.StudyMember;

import java.time.LocalDateTime;

public record StudyMineItemResponse(
        Long studyId,
        String title,
        String description,
        String role,
        LocalDateTime joinedAt
) {
    public StudyMineItemResponse(StudyMember member) {
        this(
                member.getStudy().getId(),
                member.getStudy().getTitle(),
                member.getStudy().getDescription(),
                member.getRole().name(),
                member.getJoinedAt()
        );
    }
}

