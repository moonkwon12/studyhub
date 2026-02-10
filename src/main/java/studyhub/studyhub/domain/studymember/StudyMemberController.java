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
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;
import studyhub.studyhub.global.error.ErrorResponse;

import java.util.List;

@Tag(name = "StudyMember", description = "스터디 멤버 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies/{studyId}/members")
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    // =========================
    // 스터디 참여
    // =========================
    @Operation(
            summary = "스터디 참여",
            description = "사용자가 스터디에 참여합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "스터디 참여 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 참여한 스터디",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 또는 스터디를 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Void> joinStudy(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "참여할 사용자 ID", example = "2")
            @RequestParam Long userId
    ) {
        studyMemberService.join(userId, studyId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // =========================
    // 스터디 탈퇴
    // =========================
    @Operation(
            summary = "스터디 탈퇴",
            description = "스터디 멤버가 스터디에서 탈퇴합니다. (리더는 탈퇴 불가)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "리더는 탈퇴할 수 없음")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> leaveStudy(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "탈퇴할 사용자 ID", example = "2")
            @PathVariable Long userId
    ) {
        studyMemberService.leave(userId, studyId);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // 스터디 멤버 목록 조회
    // =========================
    @Operation(
            summary = "스터디 멤버 목록 조회",
            description = "스터디에 참여 중인 모든 멤버를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    schema = @Schema(implementation = StudyMemberResponse.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<StudyMemberResponse>> getMembers(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId
    ) {
        List<StudyMemberResponse> responses = studyMemberService
                .findMembers(studyId)
                .stream()
                .map(StudyMemberResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    // =========================
    // 리더 위임
    // =========================
    @Operation(
            summary = "리더 위임",
            description = "현재 리더가 다른 멤버에게 리더 권한을 위임합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리더 위임 성공"),
            @ApiResponse(responseCode = "403", description = "리더만 위임 가능")
    })
    @PatchMapping("/{userId}/leader")
    public ResponseEntity<Void> changeLeader(
            @Parameter(description = "스터디 ID", example = "1")
            @PathVariable Long studyId,

            @Parameter(description = "현재 리더 사용자 ID", example = "1")
            @RequestParam Long leaderId,

            @Parameter(description = "새 리더가 될 사용자 ID", example = "2")
            @PathVariable Long userId
    ) {
        studyMemberService.changeLeader(leaderId, userId, studyId);
        return ResponseEntity.ok().build();
    }
}