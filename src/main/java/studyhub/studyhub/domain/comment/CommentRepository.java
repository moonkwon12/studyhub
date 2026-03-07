package studyhub.studyhub.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
        select c
        from Comment c
        join fetch c.author a
        where c.post.id = :postId
        order by c.createdAt asc
    """)
    List<Comment> findWithAuthorByPostId(@Param("postId") Long postId);

    @Query("""
        select c
        from Comment c
        join fetch c.post p
        join fetch c.author a
        where c.id = :commentId
    """)
    Optional<Comment> findDetailForAuth(@Param("commentId") Long commentId);
}
