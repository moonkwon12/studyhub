package studyhub.studyhub.domain.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.studymember.StudyMember;
import studyhub.studyhub.domain.studymember.StudyMemberRepository;
import studyhub.studyhub.domain.studymember.StudyRole;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyServiceTest {

    @Autowired StudyService studyService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudyMemberRepository studyMemberRepository;
    @Autowired StudyRepository studyRepository;

    @Test
    public void create_study_test() {
        User user = userRepository.save(
                User.create("test@test.com", "1234", "테스트")
        );

        Long studyId = studyService.createStudy(user.getId(), "JPA 스터디", "서비스 계층");

        assertThat(studyId).isNotNull();
        assertThat(studyMemberRepository.findAll()).hasSize(1);
    }

    @Test
    void study_member_query_test() {
        User user1 = userRepository.save(
                User.create("a@test.com", "1234", "A")
        );
        User user2 = userRepository.save(
                User.create("b@test.com", "1234", "B")
        );

        Study study = studyRepository.save(
                Study.create("조회 스터디", "N+1 테스트")
        );

        studyMemberRepository.save(
                StudyMember.create(user1, study, StudyRole.MEMBER)
        );
        studyMemberRepository.save(
                StudyMember.create(user2, study, StudyRole.MEMBER)
        );

//        List<StudyMember> members =
//                studyService.getStudyMembers(study.getId());
//
//        for (StudyMember member : members) {
//            System.out.println(member.getUser().getName());
//        }
    }

}