package studyhub.studyhub.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 단순 텍스트 메시지 전송 요청.
public record ChatMessageSendRequest(
        @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
        @Size(max = 1000, message = "메시지는 1000자 이하만 가능합니다.")
        String content
) {
}
