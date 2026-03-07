package studyhub.studyhub.domain.friend.dto;

public record FriendSimpleResponse(
        Long userId,
        String name,
        String email
) {
}

