package studyhub.studyhub.domain.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.chat.dto.ChatMessageResponse;
import studyhub.studyhub.domain.chat.dto.ChatMessageSendRequest;
import studyhub.studyhub.domain.chat.dto.ChatRoomResponse;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@Tag(name = "Chat API", description = "Friend-only direct chat API")
// 인증 사용자 기준으로 친구 전용 1:1 채팅 API를 노출한다.
public class ChatController {

    private final ChatService chatService;
    private final AuthUserIdResolver authUserIdResolver;

    @Operation(summary = "Create or open a direct chat room with a friend")
    @PostMapping("/direct/{friendId}")
    public ResponseEntity<ChatRoomResponse> openDirectRoom(
            @PathVariable Long friendId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(chatService.openDirectRoom(loginUserId, friendId));
    }

    @Operation(summary = "Get my chat rooms")
    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getRooms(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(chatService.getRooms(loginUserId));
    }

    @Operation(summary = "Get messages in a room")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(chatService.getMessages(roomId, loginUserId));
    }

    @Operation(summary = "Send a message")
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @PathVariable Long roomId,
            @Valid @RequestBody ChatMessageSendRequest request,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        ChatMessageResponse response = chatService.sendMessage(roomId, loginUserId, request.content().trim());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
