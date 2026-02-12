package studyhub.studyhub.domain.comment;

import jakarta.persistence.*;
import lombok.*;
import studyhub.studyhub.domain.post.StudyPost;
import studyhub.studyhub.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment",
        indexes = {
                @Index(name = "idx_comment_post_created", columnList = "post_id, created_at"),
                @Index(name = "idx_comment_author", columnList = "author_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글은 게시글에 종속
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private StudyPost post;

    // 댓글 작성자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Comment create(StudyPost post, User author, String content) {
        return Comment.builder()
                .post(post)
                .author(author)
                .content(content)
                .build();
    }
}
