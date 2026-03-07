package studyhub.studyhub.domain.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.comment.dto.CommentCreateRequest;
import studyhub.studyhub.domain.comment.dto.CommentCreateResponse;
import studyhub.studyhub.domain.comment.dto.CommentListItem;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/{studyId}/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthUserIdResolver authUserIdResolver;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> create(
            @PathVariable Long studyId,
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        CommentCreateResponse response = commentService.createComment(studyId, postId, loginUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentListItem>> list(
            @PathVariable Long studyId,
            @PathVariable Long postId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(commentService.getComments(studyId, postId, loginUserId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long studyId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        commentService.deleteComment(studyId, postId, commentId, loginUserId);
        return ResponseEntity.noContent().build();
    }
}

