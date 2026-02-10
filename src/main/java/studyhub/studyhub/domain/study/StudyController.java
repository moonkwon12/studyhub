package studyhub.studyhub.domain.study;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.study.dto.StudyCreateRequest;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;

import java.util.List;

@Tag(name = "Study API", description = "스터디 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;

    /**
     * 스터디 생성
     */
    @Operation(
            summary = "스터디 생성",
            description = "사용자가 새로운 스터디를 생성합니다."
    )
    @PostMapping
    public ResponseEntity<Long> createStudy(
            @RequestBody StudyCreateRequest request
    ) {
        Long studyId = studyService.createStudy(
                request.getUserId(),
                request.getTitle(),
                request.getDescription()
        );

        return ResponseEntity.ok(studyId);
    }

    /**
     * 스터디 멤버 조회
     */
    @Operation(
            summary = "스터디 멤버 조회",
            description = "스터디에 참여 중인 멤버 목록을 조회합니다."
    )
    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @PathVariable Long studyId
    ) {
        List<StudyMemberResponse> responses = studyService.getStudyMemberResponse(studyId);

        return ResponseEntity.ok(responses);
    }

}
