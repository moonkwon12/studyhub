package studyhub.studyhub.domain.studymember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    @Query("""
        select sm
            from StudyMember sm
                join fetch sm.user
                    where sm.study.id = :studyId    
    """)
    List<StudyMember> findWithUserByStudyId(Long studyId);

    @Query("""
        select sm
        from StudyMember sm
        join fetch sm.study
        where sm.user.id = :userId
        order by sm.joinedAt desc
    """)
    List<StudyMember> findWithStudyByUserId(Long userId);

    Optional<StudyMember> findByUserIdAndStudyId(Long userId, Long studyId);

    Optional<StudyMember> findByStudyIdAndUserId(Long studyId, Long userId);

    List<StudyMember> findByStudyId(Long studyId);

    boolean existsByUserIdAndStudyId(Long userId, Long studyId);
}
