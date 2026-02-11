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
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.study.dto.StudyCreateRequest;
import studyhub.studyhub.domain.study.dto.StudyCreateResponse;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;
import studyhub.studyhub.global.error.ErrorResponse;

import java.net.URI;
import java.util.List;

@Tag(name = "Study API", description = "스터디 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;

    @Operation(
            summary = "스터디 생성",
            description = "사용자가 새로운 스터디를 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "스터디 생성 성공",
                    content = @Content(schema = @Schema(implementation = StudyCreateResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (입력값 검증 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "비즈니스 충돌 (중복 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<StudyCreateResponse> createStudy(
            @Valid @RequestBody StudyCreateRequest request
    ) {
        StudyCreateResponse response = studyService.createStudy(
                request.getUserId(),
                request.getTitle(),
                request.getDescription()
        );

        return ResponseEntity
                .created(URI.create("/api/studies/" + response.getStudyId()))
                .body(response);
    }
}
