package studyhub.studyhub.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 대화 화면 렌더링용 전체 메시지 조회.
    List<ChatMessage> findByRoomIdOrderByCreatedAtAscIdAsc(Long roomId);

    // 채팅방 목록의 마지막 메시지 미리보기에 사용한다.
    Optional<ChatMessage> findTopByRoomIdOrderByCreatedAtDescIdDesc(Long roomId);
}
