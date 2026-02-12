package studyhub.studyhub.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import studyhub.studyhub.domain.post.dto.StudyPostListItem;

import java.util.List;
import java.util.Optional;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {

    @Query("""
    select new studyhub.studyhub.domain.post.dto.StudyPostListItem(
        p.id,
        p.title,
        a.name,
        (select count(c.id) from Comment c where c.post.id = p.id),
        p.createdAt
    )
    from StudyPost p
    join p.author a
    where p.study.id = :studyId
    order by p.createdAt desc
""")
    List<StudyPostListItem> findListByStudyId(@Param("studyId") Long studyId);

    @Query("""
        select p
        from StudyPost p
        join fetch p.study s
        join fetch p.author a
        where p.id = :postId
    """)
    Optional<StudyPost> findDetailForAuth(@Param("postId") Long postId);
}
