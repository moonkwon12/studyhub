package studyhub.studyhub.domain.friend;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studyhub.studyhub.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_request",
        indexes = {
                @Index(name = "idx_friend_request_requester", columnList = "requester_id, created_at"),
                @Index(name = "idx_friend_request_receiver", columnList = "receiver_id, created_at"),
                @Index(name = "idx_friend_request_status", columnList = "status")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FriendRequestStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    public static FriendRequest create(User requester, User receiver) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.requester = requester;
        friendRequest.receiver = receiver;
        friendRequest.status = FriendRequestStatus.PENDING;
        return friendRequest;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void accept() {
        this.status = FriendRequestStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = FriendRequestStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }
}

