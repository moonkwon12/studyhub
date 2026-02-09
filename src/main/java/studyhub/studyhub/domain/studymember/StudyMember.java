package studyhub.studyhub.domain.studymember;

import jakarta.persistence.*;
import lombok.Getter;
import studyhub.studyhub.domain.study.Study;
import studyhub.studyhub.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(
        name="study_member",
        uniqueConstraints= {
                @UniqueConstraint(columnNames = {"user_id", "study_id"})
        }
)
@Getter
public class StudyMember {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyRole role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    protected StudyMember() {
    }

    public StudyMember(User user, Study study, StudyRole role) {
        this.user = user;
        this.study = study;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    public static StudyMember create(User user, Study study, StudyRole role){
        return new StudyMember(user,study,role);
    }
}
