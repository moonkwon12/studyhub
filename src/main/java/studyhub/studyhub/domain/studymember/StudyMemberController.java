package studyhub.studyhub.domain.studymember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;
import studyhub.studyhub.global.error.ErrorResponse;
import studyhub.studyhub.global.security.AuthUserIdResolver;
import studyhub.studyhub.global.security.JwtUserPrincipal;

import java.util.List;

@Tag(name = "StudyMember", description = "Study member management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/{studyId}/members")
public class StudyMemberController {

    private final StudyMemberService studyMemberService;
    private final AuthUserIdResolver authUserIdResolver;

    @Operation(summary = "Join study")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Joined"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Already joined",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> joinStudy(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        studyMemberService.join(loginUserId, studyId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Leave study")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Left"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Leader cannot leave",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> leaveStudy(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        studyMemberService.leave(loginUserId, studyId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get study members")
    @GetMapping
    public ResponseEntity<List<StudyMemberResponse>> getMembers(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(studyMemberService.findMembers(studyId));
    }

    @Operation(summary = "Transfer leader")
    @PatchMapping("/{userId}/leader")
    public ResponseEntity<Void> changeLeader(
            @Parameter(description = "Study ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "New leader user ID", example = "2")
            @PathVariable Long userId,

            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        Long loginUserId = authUserIdResolver.resolve(principal);
        studyMemberService.changeLeader(loginUserId, userId, studyId);
        return ResponseEntity.ok().build();
    }
}
