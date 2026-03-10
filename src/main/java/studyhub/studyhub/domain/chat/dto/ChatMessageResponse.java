package studyhub.studyhub.domain.chat.dto;

import studyhub.studyhub.domain.chat.ChatMessage;

import java.time.LocalDateTime;

// 메시지 목록 렌더링용 응답.
public record ChatMessageResponse(
        Long messageId,
        Long senderId,
        String senderName,
        String content,
        LocalDateTime createdAt
) {
    public ChatMessageResponse(ChatMessage message) {
        this(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
