package studyhub.studyhub.domain.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.study.dto.StudyCreateResponse;
import studyhub.studyhub.domain.study.dto.StudyDetailResponse;
import studyhub.studyhub.domain.study.dto.StudyMineItemResponse;
import studyhub.studyhub.domain.studymember.StudyMember;
import studyhub.studyhub.domain.studymember.StudyMemberRepository;
import studyhub.studyhub.domain.studymember.Role;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.StudyNotFoundException;
import studyhub.studyhub.global.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;

    /**
     * 스터디 생성
     */
    public StudyCreateResponse createStudy(Long userId, String title, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Study study = new Study(title, description);
        studyRepository.save(study);

        StudyMember leader = new StudyMember(user, study, Role.LEADER);
        studyMemberRepository.save(leader);

        return new StudyCreateResponse(study.getId(), study.getTitle());
    }

    @Transactional(readOnly = true)
    public List<StudyMineItemResponse> findMyStudies(Long userId) {
        // Ensure request user exists before listing memberships.
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return studyMemberRepository.findWithStudyByUserId(userId)
                .stream()
                .map(StudyMineItemResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudyDetailResponse findStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);

        return new StudyDetailResponse(
                study.getId(),
                study.getTitle(),
                study.getDescription(),
                study.getCreatedAt()
        );
    }

}
