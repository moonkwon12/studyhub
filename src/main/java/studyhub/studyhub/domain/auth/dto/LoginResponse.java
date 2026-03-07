package studyhub.studyhub.domain.auth.dto;

public record LoginResponse(
        String tokenType,
        String accessToken,
        long expiresInMs,
        Long userId,
        String email
) {
}

