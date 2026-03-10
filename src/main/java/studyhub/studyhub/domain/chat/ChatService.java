package studyhub.studyhub.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.chat.dto.ChatMessageResponse;
import studyhub.studyhub.domain.chat.dto.ChatRoomResponse;
import studyhub.studyhub.domain.friend.FriendRequest;
import studyhub.studyhub.domain.friend.FriendRequestRepository;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.ChatAccessDeniedException;
import studyhub.studyhub.global.exception.ChatOnlyForFriendsException;
import studyhub.studyhub.global.exception.ChatRoomNotFoundException;
import studyhub.studyhub.global.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
// 친구 관계 검증, 채팅방 생성/조회, 메시지 저장을 담당한다.
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    // 이미 방이 있으면 재사용하고, 없으면 친구 관계를 확인한 뒤 새로 만든다.
    public ChatRoomResponse openDirectRoom(Long loginUserId, Long friendId) {
        User loginUser = userRepository.findById(loginUserId)
                .orElseThrow(UserNotFoundException::new);
        User friend = userRepository.findById(friendId)
                .orElseThrow(UserNotFoundException::new);

        if (loginUserId.equals(friendId)) {
            throw new ChatOnlyForFriendsException();
        }

        ensureFriends(loginUserId, friendId);

        ParticipantPair pair = ParticipantPair.of(loginUser, friend);
        ChatRoom room = chatRoomRepository.findByParticipantIds(pair.userA().getId(), pair.userB().getId())
                .orElseGet(() -> chatRoomRepository.save(new ChatRoom(pair.userA(), pair.userB())));

        return toRoomResponse(room, loginUserId);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getRooms(Long loginUserId) {
        userRepository.findById(loginUserId)
                .orElseThrow(UserNotFoundException::new);

        return chatRoomRepository.findAllByUserId(loginUserId)
                .stream()
                .map(room -> toRoomResponse(room, loginUserId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long roomId, Long loginUserId) {
        ChatRoom room = getAccessibleRoom(roomId, loginUserId);
        return chatMessageRepository.findByRoomIdOrderByCreatedAtAscIdAsc(room.getId())
                .stream()
                .map(ChatMessageResponse::new)
                .toList();
    }

    public ChatMessageResponse sendMessage(Long roomId, Long loginUserId, String content) {
        ChatRoom room = getAccessibleRoom(roomId, loginUserId);
        User sender = userRepository.findById(loginUserId)
                .orElseThrow(UserNotFoundException::new);

        ChatMessage message = chatMessageRepository.save(new ChatMessage(room, sender, content));
        return new ChatMessageResponse(message);
    }

    private void ensureFriends(Long userId, Long friendId) {
        FriendRequest accepted = friendRequestRepository.findAcceptedBetweenUsers(userId, friendId)
                .orElseThrow(ChatOnlyForFriendsException::new);

        if (accepted.getRequester() == null || accepted.getReceiver() == null) {
            throw new ChatOnlyForFriendsException();
        }
    }

    private ChatRoom getAccessibleRoom(Long roomId, Long loginUserId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        if (!room.containsUser(loginUserId)) {
            throw new ChatAccessDeniedException();
        }
        return room;
    }

    private ChatRoomResponse toRoomResponse(ChatRoom room, Long loginUserId) {
        User other = room.otherParticipant(loginUserId);
        ChatMessage lastMessage = chatMessageRepository.findTopByRoomIdOrderByCreatedAtDescIdDesc(room.getId())
                .orElse(null);

        return new ChatRoomResponse(
                room.getId(),
                other.getId(),
                other.getName(),
                other.getEmail(),
                room.getCreatedAt(),
                lastMessage == null ? null : lastMessage.getContent(),
                lastMessage == null ? null : lastMessage.getCreatedAt()
        );
    }

    private record ParticipantPair(User userA, User userB) {
        // 유니크 제약 조건과 동일한 순서를 보장해서 중복 채팅방 생성을 막는다.
        private static ParticipantPair of(User first, User second) {
            if (first.getId() <= second.getId()) {
                return new ParticipantPair(first, second);
            }
            return new ParticipantPair(second, first);
        }
    }
}
