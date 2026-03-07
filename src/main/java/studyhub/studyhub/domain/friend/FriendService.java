package studyhub.studyhub.domain.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.friend.dto.FriendRequestResponse;
import studyhub.studyhub.domain.friend.dto.FriendSimpleResponse;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendRequestResponse sendRequest(Long requesterId, Long receiverId) {
        if (requesterId.equals(receiverId)) {
            throw new CannotFriendYourselfException();
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(UserNotFoundException::new);

        List<FriendRequest> between = friendRequestRepository.findBetweenUsers(requesterId, receiverId);
        if (!between.isEmpty()) {
            FriendRequest latest = between.getFirst();
            if (latest.getStatus() == FriendRequestStatus.PENDING) {
                throw new FriendRequestAlreadyPendingException();
            }
            if (latest.getStatus() == FriendRequestStatus.ACCEPTED) {
                throw new AlreadyFriendsException();
            }
        }

        FriendRequest request = friendRequestRepository.save(FriendRequest.create(requester, receiver));
        return new FriendRequestResponse(request);
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getIncomingRequests(Long loginUserId) {
        return friendRequestRepository.findIncomingByReceiverId(loginUserId)
                .stream()
                .map(FriendRequestResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getOutgoingRequests(Long loginUserId) {
        return friendRequestRepository.findOutgoingByRequesterId(loginUserId)
                .stream()
                .map(FriendRequestResponse::new)
                .toList();
    }

    public FriendRequestResponse acceptRequest(Long requestId, Long loginUserId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(FriendRequestNotFoundException::new);

        if (!request.getReceiver().getId().equals(loginUserId)) {
            throw new FriendRequestForbiddenException();
        }
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new FriendRequestAlreadyHandledException();
        }

        request.accept();
        return new FriendRequestResponse(request);
    }

    public FriendRequestResponse rejectRequest(Long requestId, Long loginUserId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(FriendRequestNotFoundException::new);

        if (!request.getReceiver().getId().equals(loginUserId)) {
            throw new FriendRequestForbiddenException();
        }
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new FriendRequestAlreadyHandledException();
        }

        request.reject();
        return new FriendRequestResponse(request);
    }

    @Transactional(readOnly = true)
    public List<FriendSimpleResponse> getFriends(Long loginUserId) {
        return friendRequestRepository.findAcceptedByUserId(loginUserId)
                .stream()
                .map(fr -> {
                    User friend = fr.getRequester().getId().equals(loginUserId)
                            ? fr.getReceiver()
                            : fr.getRequester();
                    return new FriendSimpleResponse(friend.getId(), friend.getName(), friend.getEmail());
                })
                .toList();
    }
}

