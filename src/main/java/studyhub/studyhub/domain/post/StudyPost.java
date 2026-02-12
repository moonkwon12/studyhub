package studyhub.studyhub.domain.post;

import jakarta.persistence.*;
import lombok.*;
import studyhub.studyhub.domain.study.Study;
import studyhub.studyhub.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_post",
        indexes = {
                @Index(name = "idx_post_study_created", columnList = "study_id, created_at"),
                @Index(name = "idx_post_author", columnList = "author_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StudyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글은 스터디에 종속
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 확장 포인트(공지/조회수/수정시간 등)
    // private boolean notice;
    // private long viewCount;
    // private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static StudyPost create(Study study, User author, String title, String content) {
        return StudyPost.builder()
                .study(study)
                .author(author)
                .title(title)
                .content(content)
                .build();
    }
}
