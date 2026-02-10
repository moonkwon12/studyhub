package studyhub.studyhub.domain.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.studymember.StudyMember;
import studyhub.studyhub.domain.studymember.StudyMemberRepository;
import studyhub.studyhub.domain.studymember.Role;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.AlreadyJoinedStudyException;
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
     * - 생성자는 자동으로 LEADER가 된다
     */
    public Long createStudy(Long userId, String title, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Study study = new Study(title, description);
        studyRepository.save(study);

        StudyMember leader = new StudyMember(user, study, Role.LEADER);
        studyMemberRepository.save(leader);

        return study.getId();
    }

    /**
     * 스터디 참여
     */
    public void joinStudy(Long userId, Long studyId) {
        if (studyMemberRepository.findByUserIdAndStudyId(userId, studyId).isPresent()) {
            throw new AlreadyJoinedStudyException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);

        StudyMember member = new StudyMember(user, study, Role.MEMBER);
        studyMemberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<StudyMemberResponse> getStudyMemberResponse(Long studyId) {
        return studyMemberRepository
                .findWithUserByStudyId(studyId)
                .stream()
                .map(StudyMemberResponse::new)
                .toList();
    }
}
