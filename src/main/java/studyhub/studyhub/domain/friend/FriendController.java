package studyhub.studyhub.domain.friend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.friend.dto.FriendRequestCreateRequest;
import studyhub.studyhub.domain.friend.dto.FriendRequestResponse;
import studyhub.studyhub.domain.friend.dto.FriendSimpleResponse;
import studyhub.studyhub.global.error.ErrorResponse;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
@Tag(name = "Friend API", description = "친구 요청/수락/목록 API")
public class FriendController {

    private final FriendService friendService;
    private final AuthUserIdResolver authUserIdResolver;

    @Operation(summary = "친구 요청 보내기")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "친구 요청 성공",
                    content = @Content(schema = @Schema(implementation = FriendRequestResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 친구이거나 요청 대기 중",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/requests")
    public ResponseEntity<FriendRequestResponse> sendRequest(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody FriendRequestCreateRequest request
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        FriendRequestResponse response = friendService.sendRequest(loginUserId, request.receiverId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "받은 친구 요청 목록")
    @GetMapping("/requests/incoming")
    public ResponseEntity<List<FriendRequestResponse>> getIncomingRequests(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(friendService.getIncomingRequests(loginUserId));
    }

    @Operation(summary = "보낸 친구 요청 목록")
    @GetMapping("/requests/outgoing")
    public ResponseEntity<List<FriendRequestResponse>> getOutgoingRequests(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(friendService.getOutgoingRequests(loginUserId));
    }

    @Operation(summary = "친구 요청 수락")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수락 성공",
                    content = @Content(schema = @Schema(implementation = FriendRequestResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/requests/{requestId}/accept")
    public ResponseEntity<FriendRequestResponse> acceptRequest(
            @Parameter(description = "요청 ID", example = "1")
            @PathVariable Long requestId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(friendService.acceptRequest(requestId, loginUserId));
    }

    @Operation(summary = "친구 요청 거절")
    @PatchMapping("/requests/{requestId}/reject")
    public ResponseEntity<FriendRequestResponse> rejectRequest(
            @Parameter(description = "요청 ID", example = "1")
            @PathVariable Long requestId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(friendService.rejectRequest(requestId, loginUserId));
    }

    @Operation(summary = "친구 목록 조회")
    @GetMapping
    public ResponseEntity<List<FriendSimpleResponse>> getFriends(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(friendService.getFriends(loginUserId));
    }
}

