package studyhub.studyhub.domain.chat.dto;

import java.time.LocalDateTime;

// 채팅방 목록 화면에 필요한 상대 정보와 마지막 메시지 요약.
public record ChatRoomResponse(
        Long roomId,
        Long friendId,
        String friendName,
        String friendEmail,
        LocalDateTime createdAt,
        String lastMessage,
        LocalDateTime lastMessageAt
) {
}
