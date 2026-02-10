package studyhub.studyhub.domain.studymember;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studyhub.studyhub.domain.study.Study;
import studyhub.studyhub.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }

    public StudyMember(User user, Study study, Role role) {
        this.user = user;
        this.study = study;
        this.role = role;
    }

    public boolean isLeader() {
        return this.role == Role.LEADER;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}