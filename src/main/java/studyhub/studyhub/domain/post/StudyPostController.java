package studyhub.studyhub.domain.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studyhub.studyhub.domain.post.dto.StudyPostCreateRequest;
import studyhub.studyhub.domain.post.dto.StudyPostCreateResponse;
import studyhub.studyhub.domain.post.dto.StudyPostListItem;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/{studyId}/posts")
@Tag(name = "Study Post API", description = "Study post management API")
public class StudyPostController {

    private final StudyPostService postService;
    private final AuthUserIdResolver authUserIdResolver;

    @Operation(summary = "Create a post")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = StudyPostCreateResponse.class)))
    @PostMapping
    public StudyPostCreateResponse create(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId,

            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody StudyPostCreateRequest request
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return postService.createPost(studyId, loginUserId, request);
    }

    @Operation(summary = "List posts")
    @GetMapping
    public List<StudyPostListItem> list(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId,

            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return postService.getPostList(studyId, loginUserId);
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{postId}")
    public void delete(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "Post ID", example = "5")
            @PathVariable Long postId,

            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        postService.deletePost(postId, loginUserId);
    }
}
