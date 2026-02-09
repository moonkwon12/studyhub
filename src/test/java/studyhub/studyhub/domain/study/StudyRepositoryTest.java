package studyhub.studyhub.domain.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyRepositoryTest {

    @Autowired StudyRepository studyRepository;

    @Test
    public void study_save_test() {
        Study study = Study.create("JPA 스터디", "연관관계 집중 학습");

        studyRepository.save(study);

        Assertions.assertThat(study.getId()).isNotNull();
    }
}