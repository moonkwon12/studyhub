package studyhub.studyhub.domain.studymember.dto;

import lombok.Getter;
import studyhub.studyhub.domain.studymember.Role;
import studyhub.studyhub.domain.studymember.StudyMember;

@Getter
public class StudyMemberSimpleResponse {

    private final Long userId;
    private final Role role;

    public StudyMemberSimpleResponse(StudyMember member) {
        this.userId = member.getUser().getId();
        this.role = member.getRole();
    }
}