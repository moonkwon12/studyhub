package studyhub.studyhub.domain.studymember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.study.Study;
import studyhub.studyhub.domain.study.StudyRepository;
import studyhub.studyhub.domain.studymember.dto.StudyMemberResponse;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    /* ====== 스터디 참여 ====== */
    public void join(Long userId, Long studyId) {
        if (studyMemberRepository.existsByUserIdAndStudyId(userId, studyId)) {
            throw new AlreadyJoinedStudyException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);

        StudyMember member = new StudyMember(user, study, Role.MEMBER);
        studyMemberRepository.save(member);
    }

    /* ====== 스터디 생성 시 리더 등록 ====== */
    public void createLeader(Long userId, Long studyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);

        StudyMember leader = new StudyMember(user, study, Role.LEADER);
        studyMemberRepository.save(leader);
    }

    /* ====== 스터디 탈퇴 ====== */
    public void leave(Long userId, Long studyId) {
        StudyMember member = studyMemberRepository
                .findByUserIdAndStudyId(userId, studyId)
                .orElseThrow(StudyMemberNotFoundException::new);

        if (member.isLeader()) {
            throw new LeaderCannotLeaveException();
        }

        studyMemberRepository.delete(member);
    }

    /* ====== 역할 변경 (리더 위임) ====== */
    public void changeLeader(Long leaderId, Long targetUserId, Long studyId) {
        StudyMember leader = studyMemberRepository
                .findByUserIdAndStudyId(leaderId, studyId)
                .orElseThrow(StudyMemberNotFoundException::new);

        if (!leader.isLeader()) {
            throw new NotStudyLeaderException();
        }

        StudyMember target = studyMemberRepository
                .findByUserIdAndStudyId(targetUserId, studyId)
                .orElseThrow(StudyMemberNotFoundException::new);

        leader.changeRole(Role.MEMBER);
        target.changeRole(Role.LEADER);
    }

    @Transactional(readOnly = true)
    public List<StudyMemberResponse> findMembers(Long studyId) {
        List<StudyMember> members =
                studyMemberRepository.findWithUserByStudyId(studyId);

        return members.stream()
                .map(StudyMemberResponse::new)
                .toList();
    }

}