package studyhub.studyhub.domain.studymember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import studyhub.studyhub.domain.studymember.StudyMember;
import studyhub.studyhub.domain.studymember.Role;

import java.time.LocalDateTime;

@Schema(description = "스터디 멤버 응답 DTO")
@Getter
public class StudyMemberResponse {

    @Schema(description = "사용자 이름", example = "홍길동")
    private final String userName;

    @Schema(description = "사용자 이메일", example = "hong@test.com")
    private final String userEmail;

    @Schema(description = "스터디 내 역할", example = "MEMBER")
    private final Role role;

    @Schema(description = "스터디 참여 시각")
    private final LocalDateTime joinedAt;

    public StudyMemberResponse(StudyMember member) {
        this.userName = member.getUser().getName();
        this.userEmail = member.getUser().getEmail();
        this.role = member.getRole();
        this.joinedAt = member.getJoinedAt();
    }
}
