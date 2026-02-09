package studyhub.studyhub.domain.studymember.dto;

import lombok.Getter;
import studyhub.studyhub.domain.studymember.StudyMember;
import studyhub.studyhub.domain.studymember.StudyRole;

import java.time.LocalDateTime;

@Getter
public class StudyMemberResponse {

    private final String userName;
    private final String userEmail;
    private final StudyRole role;
    private final LocalDateTime joinedAt;

    public StudyMemberResponse(StudyMember member) {
        this.userName = member.getUser().getName();
        this.userEmail = member.getUser().getEmail();
        this.role = member.getRole();
        this.joinedAt = member.getJoinedAt();
    }
}
