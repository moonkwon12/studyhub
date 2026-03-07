package studyhub.studyhub.domain.friend.dto;

import jakarta.validation.constraints.NotNull;

public record FriendRequestCreateRequest(
        @NotNull(message = "receiverId is required.")
        Long receiverId
) {
}

