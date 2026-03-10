package studyhub.studyhub.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // userA/userB 정렬 규칙으로 저장된 1:1 채팅방을 찾는다.
    @Query("""
        select cr
        from ChatRoom cr
        join fetch cr.userA ua
        join fetch cr.userB ub
        where ua.id = :userAId and ub.id = :userBId
    """)
    Optional<ChatRoom> findByParticipantIds(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

    // 현재 사용자가 참여 중인 채팅방 목록을 조회한다.
    @Query("""
        select cr
        from ChatRoom cr
        join fetch cr.userA ua
        join fetch cr.userB ub
        where ua.id = :userId or ub.id = :userId
        order by cr.createdAt desc
    """)
    List<ChatRoom> findAllByUserId(@Param("userId") Long userId);
}
