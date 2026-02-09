package studyhub.studyhub.domain.study;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.study.dto.StudyCreateRequest;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;

    /**
     * 스터디 생성
     */
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
     * 스터디 참여
     */
    @PostMapping("/{studyId}/join")
    public ResponseEntity<Void> joinStudy(
            @PathVariable Long studyId,
            @RequestParam Long userId
    ) {
        studyService.joinStudy(userId, studyId);
        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 멤버 조회
     */
    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @PathVariable Long studyId
    ) {
        List<StudyMemberResponse> responses = studyService.getStudyMemberResponse(studyId);

        return ResponseEntity.ok(responses);
    }

}
