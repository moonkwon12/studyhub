package studyhub.studyhub.domain.friend.dto;

import studyhub.studyhub.domain.friend.FriendRequest;
import studyhub.studyhub.domain.friend.FriendRequestStatus;

import java.time.LocalDateTime;

public record FriendRequestResponse(
        Long requestId,
        Long requesterId,
        String requesterName,
        Long receiverId,
        String receiverName,
        FriendRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime respondedAt
) {
    public FriendRequestResponse(FriendRequest request) {
        this(
                request.getId(),
                request.getRequester().getId(),
                request.getRequester().getName(),
                request.getReceiver().getId(),
                request.getReceiver().getName(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getRespondedAt()
        );
    }
}

