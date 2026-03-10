package studyhub.studyhub.domain.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studyhub.studyhub.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "chat_room",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_chat_room_participants", columnNames = {"user_a_id", "user_b_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 두 친구 사이의 1:1 채팅방을 나타낸다. 참여자 순서는 user id 기준으로 고정 저장된다.
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_a_id", nullable = false)
    private User userA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_b_id", nullable = false)
    private User userB;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ChatRoom(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean containsUser(Long userId) {
        return userA.getId().equals(userId) || userB.getId().equals(userId);
    }

    public User otherParticipant(Long userId) {
        if (userA.getId().equals(userId)) {
            return userB;
        }
        return userA;
    }
}
