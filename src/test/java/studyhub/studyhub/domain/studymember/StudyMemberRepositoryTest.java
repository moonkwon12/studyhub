package studyhub.studyhub.domain.studymember;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.study.Study;
import studyhub.studyhub.domain.study.StudyRepository;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyMemberRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StudyRepository studyRepository;
    @Autowired StudyMemberRepository studyMemberRepository;

    @Test
    public void study_member_save_test() {
        User user = userRepository.save(
                User.create("test@test.com", "1234", "테스트")
        );
        Study study = studyRepository.save(
                Study.create("JPA 스터디", "연관관계 학습")
        );

        StudyMember member = StudyMember.create(user, study, StudyRole.LEADER);

        studyMemberRepository.save(member);

        assertThat(member.getId()).isNotNull();
    }
}