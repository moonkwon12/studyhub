package studyhub.studyhub.domain.study;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studyhub.studyhub.domain.study.dto.StudyCreateRequest;
import studyhub.studyhub.domain.study.dto.StudyCreateResponse;
import studyhub.studyhub.domain.study.dto.StudyDetailResponse;
import studyhub.studyhub.domain.study.dto.StudyMineItemResponse;
import studyhub.studyhub.global.error.ErrorResponse;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.net.URI;
import java.util.List;

@Tag(name = "Study API", description = "Study management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;
    private final AuthUserIdResolver authUserIdResolver;

    @Operation(summary = "Create study")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(schema = @Schema(implementation = StudyCreateResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<StudyCreateResponse> createStudy(
            @Valid @RequestBody StudyCreateRequest request,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);

        StudyCreateResponse response = studyService.createStudy(
                loginUserId,
                request.getTitle(),
                request.getDescription()
        );

        return ResponseEntity
                .created(URI.create("/api/studies/" + response.getStudyId()))
                .body(response);
    }

    @Operation(summary = "Get my studies")
    @GetMapping("/mine")
    public ResponseEntity<List<StudyMineItemResponse>> getMyStudies(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        return ResponseEntity.ok(studyService.findMyStudies(loginUserId));
    }

    @Operation(summary = "Get study detail")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetailResponse> getStudyDetail(
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(studyService.findStudy(studyId));
    }
}
