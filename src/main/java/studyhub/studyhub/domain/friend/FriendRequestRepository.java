package studyhub.studyhub.domain.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("""
        select fr
        from FriendRequest fr
        join fetch fr.requester rq
        join fetch fr.receiver rc
        where (rq.id = :userA and rc.id = :userB)
           or (rq.id = :userB and rc.id = :userA)
        order by fr.createdAt desc
    """)
    List<FriendRequest> findBetweenUsers(@Param("userA") Long userA, @Param("userB") Long userB);

    @Query("""
        select fr
        from FriendRequest fr
        join fetch fr.requester rq
        join fetch fr.receiver rc
        where rc.id = :receiverId
        order by fr.createdAt desc
    """)
    List<FriendRequest> findIncomingByReceiverId(@Param("receiverId") Long receiverId);

    @Query("""
        select fr
        from FriendRequest fr
        join fetch fr.requester rq
        join fetch fr.receiver rc
        where rq.id = :requesterId
        order by fr.createdAt desc
    """)
    List<FriendRequest> findOutgoingByRequesterId(@Param("requesterId") Long requesterId);

    @Query("""
        select fr
        from FriendRequest fr
        join fetch fr.requester rq
        join fetch fr.receiver rc
        where fr.status = studyhub.studyhub.domain.friend.FriendRequestStatus.ACCEPTED
          and (rq.id = :userId or rc.id = :userId)
        order by fr.respondedAt desc
    """)
    List<FriendRequest> findAcceptedByUserId(@Param("userId") Long userId);

    // 두 사용자 사이에 수락된 친구 관계가 있는지 채팅 허용 여부 판단에 사용한다.
    @Query("""
        select fr
        from FriendRequest fr
        join fetch fr.requester rq
        join fetch fr.receiver rc
        where fr.status = studyhub.studyhub.domain.friend.FriendRequestStatus.ACCEPTED
          and ((rq.id = :userA and rc.id = :userB) or (rq.id = :userB and rc.id = :userA))
        order by fr.respondedAt desc
    """)
    List<FriendRequest> findAcceptedBetweenUsersAll(@Param("userA") Long userA, @Param("userB") Long userB);

    default Optional<FriendRequest> findAcceptedBetweenUsers(Long userA, Long userB) {
        List<FriendRequest> result = findAcceptedBetweenUsersAll(userA, userB);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.getFirst());
    }
}

