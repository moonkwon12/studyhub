package studyhub.studyhub.domain.studymember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import studyhub.studyhub.domain.study.Study;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    Optional<StudyMember> findByUserIdAndStudyId (Long userId, Long studyId);

    @Query("""
        select sm
            from StudyMember sm
                join fetch sm.user
                    where sm.study.id = :studyId    
    """)
    List<StudyMember> findWithUserByStudyId(Long studyId);


    List<StudyMember> findByStudyId(Long studyId);
}
