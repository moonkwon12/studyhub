package studyhub.studyhub.domain.comment;

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
import studyhub.studyhub.domain.comment.dto.CommentCreateRequest;
import studyhub.studyhub.domain.comment.dto.CommentCreateResponse;
import studyhub.studyhub.domain.comment.dto.CommentListItem;
import studyhub.studyhub.domain.comment.dto.CommentUpdateRequest;
import studyhub.studyhub.domain.comment.dto.CommentUpdateResponse;
import studyhub.studyhub.global.error.ErrorResponse;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/{studyId}/posts/{postId}/comments")
@Tag(name = "Comment API", description = "스터디 게시글 댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final AuthUserIdResolver authUserIdResolver;

    @Operation(summary = "댓글 작성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = CommentCreateResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "스터디 멤버가 아니거나 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<CommentCreateResponse> create(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,
            @Parameter(description = "게시글 ID", example = "10")
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        CommentCreateResponse response = commentService.createComment(studyId, postId, loginUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "댓글 목록 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentListItem.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "스터디 멤버가 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<CommentListItem>> list(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,
            @Parameter(description = "게시글 ID", example = "10")
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(commentService.getComments(studyId, postId, loginUserId));
    }

    @Operation(summary = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(
                    responseCode = "403",
                    description = "삭제 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,
            @Parameter(description = "게시글 ID", example = "10")
            @PathVariable Long postId,
            @Parameter(description = "댓글 ID", example = "100")
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        commentService.deleteComment(studyId, postId, commentId, loginUserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 수정 (작성자 본인만 가능)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 수정 성공",
                    content = @Content(schema = @Schema(implementation = CommentUpdateResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "수정 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> update(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,
            @Parameter(description = "게시글 ID", example = "10")
            @PathVariable Long postId,
            @Parameter(description = "댓글 ID", example = "100")
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(commentService.updateComment(studyId, postId, commentId, loginUserId, request));
    }
}
