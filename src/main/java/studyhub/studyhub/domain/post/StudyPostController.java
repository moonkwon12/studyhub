package studyhub.studyhub.domain.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.post.dto.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/{studyId}/posts")
@Tag(name = "Study Post API", description = "스터디 게시글 관리 API")
public class StudyPostController {

    private final StudyPostService postService;

    @Operation(
            summary = "게시글 작성",
            description = "스터디 멤버만 게시글을 작성할 수 있습니다."
    )
    @ApiResponse(responseCode = "200", description = "게시글 작성 성공",
            content = @Content(schema = @Schema(implementation = StudyPostCreateResponse.class)))
    @ApiResponse(responseCode = "404", description = "스터디 없음")
    @ApiResponse(responseCode = "403", description = "스터디 멤버 아님")
    @PostMapping
    public StudyPostCreateResponse create(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "로그인 사용자 ID", example = "10")
            @RequestHeader("X-USER-ID") Long loginUserId,

            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 생성 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = StudyPostCreateRequest.class))
            )
            StudyPostCreateRequest request
    ) {
        return postService.createPost(studyId, loginUserId, request);
    }

    // ---------------------------------------------------------

    @Operation(
            summary = "게시글 목록 조회",
            description = "해당 스터디의 게시글 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "403", description = "스터디 멤버 아님")
    @GetMapping
    public List<StudyPostListItem> list(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "로그인 사용자 ID", example = "10")
            @RequestHeader("X-USER-ID") Long loginUserId
    ) {
        return postService.getPostList(studyId, loginUserId);
    }

    // ---------------------------------------------------------

    @Operation(
            summary = "게시글 삭제",
            description = "작성자 또는 스터디장이 게시글을 삭제할 수 있습니다."
    )
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "404", description = "게시글 없음")
    @ApiResponse(responseCode = "403", description = "권한 없음")
    @DeleteMapping("/{postId}")
    public void delete(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "게시글 ID", example = "5")
            @PathVariable Long postId,

            @Parameter(description = "로그인 사용자 ID", example = "10")
            @RequestHeader("X-USER-ID") Long loginUserId
    ) {
        postService.deletePost(postId, loginUserId);
    }
}
